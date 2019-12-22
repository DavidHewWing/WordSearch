# WordSearch - :punch::clap:

### Features of the Word Search :tada::green_heart:
1. It has at least a 10x10 grid.
2. It contains at least **Swift, Kotlin, ObjectiveC, Variable, Java, Mobile**.
3. Keeps track of how many words a user has found.
4. Randomizes the placement of the words.
5. Swipe over the word to select it.
6. Replayable.
7. Has a slick UI with drawer menu.
8. Able to add which words you want to the word search up to a limit.
9. Able to pick a range of grid sizes 10 -> 13.

### Screen Shots :camera:
 
From the Challenge Docs :book:

| No words have been found | All words have been found |
| --- | --- |
|<img src="https://github.com/DavidHewWing/WordSearch/blob/master/pictures/unsolved.png" width="200"> | <img src="https://github.com/DavidHewWing/WordSearch/blob/master/pictures/solved.png" width="200"> |

### Some Extra Fun Features! :fire:

**The game is replayable!** :tada:

**You can swipe to select a word** :tada:

**You can choose words to add into the word search!** :tada:

| Swiping Feature | Setup | Drawer |
| --- | --- | --- |
| <img src="https://github.com/DavidHewWing/WordSearch/blob/master/pictures/demo.gif" width="200"> | <img src="https://github.com/DavidHewWing/WordSearch/blob/master/pictures/setup.png" width="200"> | <img src="https://github.com/DavidHewWing/WordSearch/blob/master/pictures/drawer.png" width="200"> |

### Tasks :pencil:
Display random letters on a grid. **(DONE)**
- Use a 10x10 grid.
- Populate letters

Display the selected words and display them on the screen. **(DONE)**
- 6 directions (backwards and vertical, backwards and horizontal, backwards and diagonal, and regular with all the directions)
- Randomize where they are placed.

Implement selecting the words **(DONE)**
- Create a swiping function
- Once swiping keep track of words it has swiped over.
- If you release, check if the combination of letters is in the selected words array.

Add the User Input **(DONE)**
- Able to add a word to the word serach
- Able to switch grid size
- Able to start a new game whenever

