# Описание

Дело в том, что у меня большая коллекция концертных записей Grateful Dead,
и в один прекрасный день мне просто надоело переименовывать треки вручную.

После каждого скачивания у меня есть какой-то такой файлик:

```
Grateful Dead - August 6, 1971
Hollywood Palladium - Hollywood, CA

Recording Info:
SBD -> Master Reel -> CD

Transfer Info:
CD -> Samplitude Professional v10.02 -> FLAC
(3 Discs Audio / 2 Discs FLAC)

All Transfers and Mastering By Charlie Miller
charliemiller87@earthlink.net
March 19, 2008

Patch Info:
(FOB) Sony ECM-22P -> Master Reel supplies:
The Other One (1:25 - 2:19)

Notes:
-- Set 2 is seamless
-- WRS Prelude tease after Me And My Uncle
-- Master Reel transferred to CD at the Mastering Plant in L.A.

Set 1:
d1t01 - Bertha
d1t02 - Playing In The Band
d1t03 - Loser
d1t04 - Mr. Charlie
d1t05 - El Paso
d1t06 - Cumberland Blues
d1t07 - Brokedown Palace
d1t08 - Me And Bobby McGee
d1t09 - Hard To Handle
d1t10 - Casey Jones

Set 2:
d2t01 - Saint Stephen
d2t02 - Truckin' ->
d2t03 - Drums ->
d2t04 - The Other One ->
d2t05 - Me And My Uncle ->
d2t06 - The Other One
d2t07 - Deal
d2t08 - Sugar Magnolia
d2t09 - Morning Dew
d3t01 - Turn On Your Lovelight
```

И я хотел автоматизировать процесс. Да, пока работает только под один тип файла,
и стандартизировать всё полностью не представляется возможным,
но жизнь для меня, как для коллекционера, становится уже существенно легче.

Ещё одна важная функция — нормализация названий в уже проименованных концертах. 
Через TreeSet считываются все уникальные имена песен (поэтому они уже отсортированы),
складываются в файл, и пользователь, то есть я, может указать через двоеточие желаемую замену названия. 
Таким образом, ведётся полный лог допустимых и недопустимых названий: 
в случае со вторыми — трек переименовывается, и в будущем, если такая ошибка снова будет допущена,
трек снова будет переименован.

Такая, во всяком случае, идея.
