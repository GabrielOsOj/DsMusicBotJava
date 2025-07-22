
# Discord Music Bot
Este es un bot de música para Discord desarrollado como proyecto personal para reforzar mis conocimientos en Java.

Permite la reproduccion de musica en canales de voz, utiliza la librerias JDA para la conexion con discord, Lavaplayer para la TRANSMISION de musica y la libreria yt-dlp creada en python para la descarga de archivos.

Funcionalidades principales:
- Soporte de múltiples comandos.
- Descarga temporal de archivos musicales.
- Reproduccion automatica en canales de voz.
- Gestion de cola de reproduccion.
- Uso de listeners personalizados.

---
## Instalacion
### Global
1. Descarga el repositorio:
`git clone https://github.com/GabrielOsOj/DsMusicBotJava.git`

2. Posiciona la consola de windows y ejecuta
`mvn clean install`

3. Ejecuta en el terminal 
`java -jar targer/discord-bot.jar`

> Cada vez que se despliegue el bot, deberas ingresar tu discord bot token.

IMPORTANTE:
Se requiere de 4 dependencias para el correcto funcionamiento:
- Python3 para la ejecucion interna de las canciones.
- yt-dlp para la descarga de archivos.
- ffmpeg y ffprobe para la correcta ejecucion de ffmpeg

---
### Docker
Es necesario tener instalado docker dentro del equipo donde se ejecutara el bot.
Tambien es necesario tener creado previamente un token de acceso para el bot, este se crea dentro del portal de desarrollador de discord, es neceseario que el token cuente con los permisos de:

- Presence Intent
- Server Members Intent
- Message Content Intent

### Ejecucion con docker
En el directorio que desees, crea un nuevo archivo de configuracion llamado:
docker-compose.yml
Abrelo y pega el siguiente codigo:

```yaml
version: '3.8'

services:
  discord_bot:
    image: gbdev001/discord_bot:latest
    container_name: discord_bot
    restart: unless-stopped
    command : //tu token creado

```
finalmente ejecuta:
```bash
docker compose up
```
para detener el bot, preciona ctrl + c 

---
## Explicación a detalle
En esta sección voy a explicar en profundidad cómo está creado y cómo trabaja internamente el bot.

#### Estructura
La estructura del bot es simple: recibe un mensaje de un canal de Discord, descarga el tema desde YouTube en un directorio temporal y, cuando la canción termina, el bot borra la canción del directorio para ahorrar espacio.

##### Listener
Primero, se encuentra la clase Listener, que se encarga de recibir el mensaje con el comando para reproducir la canción.

>Ejemplo de comando play:
>!play nombre de la canción

El comando ingresado se separa y se filtra en diferentes partes. Primero, se verifica que la cadena de texto contenga el símbolo ! (este es el predeterminado, pero se puede cambiar modificando la clase BotFinalMessages, específicamente la constante MSG_INPUT_SYMBOL). Si el mensaje contiene el símbolo, se verifica qué comando se ingresó. Actualmente, hay 4 comandos disponibles:

- !play
- !pause
- !next
- !ping

Imaginemos que se ingresó el siguiente comando:

`
!play ciudad mágica
`

El bot recibe el mensaje, detecta el comando play y también que hay más caracteres después de la palabra play, lo que indica que se desea buscar una canción para reproducir. Se toma todo lo que está luego del espacio de la palabra play y se junta respetando los espacios.

El nombre de la canción obtenido se envía, a través de un método, a la clase SongManager.

##### SongManager
Esta clase es la encargada de reproducir, encolar y gestionar el reproductor de música. Primero, se genera un nuevo Player, que se debe enlazar a un TrackScheduler (en este código, tanto el Player como el Scheduler se gestionan en la clase GuildPlayer, y esta última gestiona el SongManager).

El Player es el encargado de reproducir las canciones; recibe un AudioTrack y lo reproduce.
El Scheduler pone en cola varias canciones y gestiona los eventos que genera el Player (por ejemplo, cuando una canción termina).
Cuando llega el nombre de la canción, se envía a la clase SAD (hablaremos de ella más adelante). Esta retorna un objeto con la ruta, el título y el ID de la canción almacenada localmente.

La clase PlayerManager intenta cargar el archivo de audio. Si tiene éxito, el archivo se transforma en un AudioTrack y se envía al Scheduler. Este verifica si el Player está reproduciendo alguna canción. Si lo está, almacena la nueva canción en la cola; de lo contrario, la envía directamente al Player para que comience a reproducirla.

Al finalizar la canción, el Player emite un evento que el TrackScheduler captura para enviar otra canción de la cola. Además, se emite un evento para que la clase SAD elimine la canción del directorio temporal.

Los comandos play, pause y next son recibidos por la clase SongManager, que gestiona al Player.

##### SAD
La clase SAD (Search And Download) unifica y dirige las clases SearchManager y DownloadManager, encargadas de recibir, buscar, descargar y eliminar las canciones que el usuario solicita.

Primero, el nombre de la canción llega a la clase SAD, que lo envía a la clase SearchManager. Esta toma el nombre de la canción, lo une a la URL de búsqueda de YouTube (https://www.youtube.com/results?search_query=) y realiza una petición GET.

>Ejemplo de petición:
>https://www.youtube.com/results?search_query=ciudad+magica

Una vez obtenido el HTML del resultado de búsqueda, se busca el ID del video de YouTube (por ejemplo: V419yO6FeIU). Este ID se envía a la clase DownloadManager, donde se inicia la descarga. Al terminar, se retorna un objeto de tipo SongDownloadedFile, que contiene los datos necesarios para que el PlayerManager cargue la canción.

> Evitar anuncios:
Para evitar capturar el ID de un video de anuncio, se busca la etiqueta llamada videoRenderer. Dentro de esta, se encuentran los datos de los videos obtenidos de la búsqueda, y se usa una expresión regular para seleccionar el primer ID válido.

### Descarga de canciones temporales
Para reproducir la cancion, se toma el id proveniente de la clase SearchManager.
Internamente, la descarga se realiza mediante la libreria yt-dlp, la misma esta desarrollada en python y es ejecutada como un proceso externo en java, con esta libreria se obtiene la url del recurso de audio deseado y se descarga de forma TEMPORAL para su reproduccion en discord.

Finalmente, cuando la canción termina, se dispara un evento para que la clase SAD elimine la canción usando el path local almacenado en el objeto SongDownloadedFile.

----- 

Aclaraciones finales
Este es un proyecto que realicé para afianzar algunos conocimientos de Java SE. Es posible que existan bugs que no he detectado.
 
