# Course Ideas

Based on the existing structure (button → door interaction, Chat/Logs lib, SimpleDoor obstacle),
these 20 course ideas progress from absolute beginner Java to intermediate OOP.

---

## Basics

- [ ] **Basics 1 – Say Hello** *(already started)*
  Button press triggers `Chat.message()` and `Chat.broadcast()`. Intro to method calls and String literals.

- [ ] **Basics 2 – Variables**
  Store the pressing player's name in a variable and use it in a personalized greeting. Introduces `String`, `int`, and assignment.

- [ ] **Basics 3 – String Concatenation**
  Build messages like `"Hello, " + playerName + "!"`. Covers `+` on Strings and the difference between text and numbers.

- [ ] **Basics 4 – If / Else**
  Only open the door if it is daytime in the Minecraft world (`world.getTime() < 13000`). Intro to boolean conditions.

- [ ] **Basics 5 – Multiple Conditions**
  Open the door only for a specific player name (`&&`, `||`, `!`). Covers compound conditions and `String.equals()`.

- [ ] **Basics 6 – For Loop**
  Light up a row of sea-lanterns one by one with a `for` loop that iterates over block positions.

- [ ] **Basics 7 – While Loop**
  Broadcast a countdown ("5… 4… 3…") before a door opens. Introduces `while` and decrement operators.

- [ ] **Basics 8 – Your Own Methods**
  Extract the greeting logic into a reusable `greet(Player p)` method. Covers method definition, naming, and calling.

- [ ] **Basics 9 – Method Parameters & Return Values**
  Write an `isDaytime()` helper that returns `boolean`. Teaches parameters, `return`, and using the result in an `if`.

- [ ] **Basics 10 – Math & Arithmetic**
  Count how many times the button has been pressed and broadcast the total. Introduces `int` fields, `++`, and arithmetic operators.

---

## Intermediate

- [ ] **Intermediate 1 – Arrays**
  Maintain a fixed-size whitelist of allowed player names (`String[]`). Students check if the presser is on the list.

- [ ] **Intermediate 2 – ArrayList**
  Build a dynamic entry queue: players join via button press and get let in one at a time. Introduces `ArrayList<>`, `add()`, `remove()`, `size()`.

- [ ] **Intermediate 3 – Switch / Case**
  Add a text-command system (via the `command(String cmd)` hook) that reacts differently to `"open"`, `"close"`, `"reset"`. Covers `switch` and `default`.

- [ ] **Intermediate 4 – Creating Your Own Class**
  Build a `ScoreTracker` class with a `score` field and `addPoint()` / `getScore()` methods. Intro to classes, fields, and encapsulation.

- [ ] **Intermediate 5 – Constructors**
  Add a constructor to `ScoreTracker` that sets an initial score and a player's name. Teaches object instantiation with `new`.

- [ ] **Intermediate 6 – Inheritance**
  Create a `TimedDoor` that extends `SimpleDoor` and auto-closes after a set number of seconds. Covers `extends`, `super`, and method overriding.

- [ ] **Intermediate 7 – Interfaces**
  Make the course class implement a second interface (e.g. a chat-listener) alongside `IButtonActivated`. Teaches what interfaces are and why they exist.

- [ ] **Intermediate 8 – HashMap**
  Track each player's button-press count in a `HashMap<String, Integer>`. Display a leaderboard on request. Introduces key-value storage and iteration.

- [ ] **Intermediate 9 – Enums**
  Model door state as an enum (`LOCKED`, `CLOSED`, `OPEN`) instead of magic integers. Covers enum declaration, `switch` on enums, and why enums beat raw numbers.

- [ ] **Intermediate 10 – Scheduled Tasks / Timers**
  Use Bukkit's scheduler to auto-close the door 5 seconds after it opens. Covers lambda basics and the concept of asynchronous / delayed execution.

---

## Notes for implementation
- Each course lives in `YOURCODE/<category>/example<N>/` with an `instructions.md` and a starter `.java` file.
- A matching `courseBackEnd` class wires up the world (block positions, doors) so students only touch the logic.
- Courses should build on the previous one where possible so students see continuity.
