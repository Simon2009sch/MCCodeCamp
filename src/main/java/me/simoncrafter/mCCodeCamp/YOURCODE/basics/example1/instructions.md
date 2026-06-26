# Basics: 1
### Indroduction
In here your will learn a bit about how you can Interact with the gameworld with code.<br>
We will using the [Paper API](https://papermc.io/) witch provides a way for us <br>
to create a Programm that modefies the behavior of the server an almost every way.<br>
In this course some things will be made easier for you by only gradually teaching you<br>
about the real functions that are directly provided by Paper. In the mean time you will be<br>
using functions that were abreveated and made simpler.
___
At the start when you don't know java, you will have to just accept some parts of it<br>
like `@Override`, `public static...` and `implements`. So I recommend ignoring them<br>
---
First in this same folder as this file, there is a `Main.java` (or just `Main`) file.<br>
When you open it you will see the following function:
```java
@Override // this override is also out of scope of the basics
public void buttonPressed(Player player, Block button) {
    Chat.message("You just pressed the button that was hooked up to the code");
    Logs.info("The button was pressed!"); // equivalent to System.out.println()
}
```
Inside the Method(=function) it sends a message to the player that pressed the button<br>
and writes a simple message to the Log.
### Task 1: Add a broadcas message!
Your goal is to send a message to every player on the server (You can test this<br> 
with some friends, if you are in the same Network)<br>
For this you can use the `Chat.broadcast("Hello World")` function.<br>
Add it!