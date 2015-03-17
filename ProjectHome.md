Jurassic is a GUI frontend for fossil SCM. Why should you use it?

**I eat my own dog food.**

Jurassic was built for my own needs, and it serves me well. I tried to make it as simple and easy as possible, in order to save time when managing my source code.

**Eye candy.**

I think that a good SCM should look as if it were made by Apple. Or Microsoft. Or both. Anyway, I made sure to add as much as unnecessary eyecandy as possible.

**Open source with a friendly license.**

You can view the source, modify it, and practically do whatever you want, since it is BSD licensed.

**Built with Java.**

It runs on Windows, Mac OS, Linux and anywhere is a Java 6 compatible VM. Of course, you also need fossil.

**Get involved with the community and discuss!**

http://groups.google.com/group/jurassic-fossil

Technical notes:

A very simple Java interface for Fossil for personal use.
It does not replace Fossil's web server, instead it leverages it.

Features:
  * Automatically start fossil web server on launch and closes it on close (you must run Jurassic on the same folder where your .fossil files are)
  * Scans for open repositories (starts from parent directory and checks all the subdirectories)
  * Switch projects by clicking a combobox
  * Shortcuts for most used fossil commands, all with a Office-ish ribbon interface.

Note:
  * Tested only on Windows
  * Requires Java 6

Instructions:

Install Java, extract the archive and then double click jurassic.jar. Please note that jurassic.jar must be run on the same folder of your .fossil files, which actually must have .fossil extension.

&lt;wiki:gadget url="http://www.ohloh.net/p/485934/widgets/project\_partner\_badge.xml" height="53" border="0"/&gt;
