# Kora

![Project logo](https://github.com/gskbyte/kora/blob/master/pics/main_bg/png/kora_256.png)

An Android app to help autistic children interact with domotics

This project was developed between March and September 2010 as my final degree project. It participated in [the 4th edition of the Spanish Free Software Contest](http://www.concursosoftwarelibre.org/0910/premios-iv-concurso-universitario-software-libre.html) and got the prize for the best accessibility app.
At that time, I was using [Subversion](https://github.com/gskbyte/kora/commit/e815f80aea6c75d939f6518faeb088b58e796b86) as VCS and I brought it to git recently (2015) to make it a little bit more public.

The structure of this repository is the following:

- Kora: the Android app. Developed using Eclipse for Android 1.6 (!). I made the first development using an HTC Tatoo and an HTC Dream.
- KoraServer: the server app, writen in Java. It is able to communicate with KNX devices by using the Java library [Calimero](http://sourceforge.net/p/calimero/wiki/Home/).
- SimKora: a simulator, in case you don't have any KNX devices.

