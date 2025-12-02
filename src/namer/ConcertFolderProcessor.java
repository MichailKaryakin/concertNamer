package namer;

import java.nio.file.*;

/**
 * Класс, отвечающий за обработку папки концерта:
 *  - чтение информации о концерте
 *  - создание целевой директории с правильным названием
 *  - обработка и переименование аудио-файлов
 */
public record ConcertFolderProcessor(ConcertInfoReader infoReader, FileRenamer fileRenamer) {

    /**
     * Обрабатывает папку концерта:
     *  1. Считывает информацию о концерте (дата, место, треклист)
     *  2. Создаёт новую папку с названием "<дата> <локация>"
     *  3. Ищет все аудиофайлы и отправляет их на обработку
     */
    public void processConcert(Path folder) throws Exception {

        // Считываются метаданные концерта
        ConcertInfo info = infoReader.read(folder);

        // Формируется новое имя папки: "YYYY-MM-DD City, State"
        String newName = info.date() + " " + info.location();
        Path targetRoot = folder.getParent().resolve(newName);

        // Создаётся целевая директория (если её нет)
        Files.createDirectories(targetRoot);

        // Запускается стрим файлов и фильтруется так, чтобы прогонялись только lossless файлы с музыкой
        try (var stream = Files.list(folder)) {
            stream.filter(p -> p.toString().matches("(?i).+\\.(flac|wav)$"))
                    .forEach(file -> processAudio(file, info, targetRoot));
        }
    }

    /**
     * Обрабатывает один аудиофайл:
     *  - извлекает из имени композитный номер (d1t05)
     *  - ищет соответствующее название трека
     *  - передаёт файл на переименование
     */
    private void processAudio(Path file, ConcertInfo info, Path targetRoot) {
        try {
            String name = file.getFileName().toString().toLowerCase();

            // Попытка вытащить "dX tY" из имени файла
            String trackData = name.matches(".*d(\\d+)t(\\d+).*")
                    ? name.replaceAll(".*d(\\d+)t(\\d+).*", "$1 $2")
                    : null;

            // Если формат не совпадает, файл пропускается
            if (trackData == null) return;

            // Разнос номеров диска и трека
            String[] splitData = trackData.split(" ");
            int disc = Integer.parseInt(splitData[0]);
            int track = Integer.parseInt(splitData[1]);

            // Формирование ключа, по которому будет извлекаться трек
            String key = String.format("d%dt%02d", disc, track);

            // Получение нормализованного названия трека (или Unknown)
            String title = info.tracks().getOrDefault(key, "Unknown");

            // Выполняется переименование
            fileRenamer.renameFile(file, title, disc, track, targetRoot);

        } catch (Exception e) {
            System.out.println("Error processing the file " + file + ": " + e.getMessage());
        }
    }
}
