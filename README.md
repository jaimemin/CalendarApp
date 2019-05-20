# CalendarApp

Programmers Summer Internship Task

Overview of the app workflow
1. After 1.3 seconds, the app will enter the main screen(MainActivity).
2. The main screen consists of 3 fragments.(monthly fragment, weekly fragment, daily fragment)
3. Each fragment consists of 2 buttons(previous button, next button), title, and gridlayout.
4. Monthly fragment's gridlayout shows the current month calendar, weekly fragment's gridlayout shows the week calendar as of current day, and daily fragment's gridlayout shows the current day's whole content of schedules.</br>
4.1 Since monthly fragment and weekly fragment's gridlayout is made up of several cells, each cell doesn't show the whole content of schedules.</br>
4.2 Each monthly fragment's gridlayout's cell shows the number of schedules.(If there is no schedule on that day, instead of marking +0, nothing is indicated for that cell.)</br>
4.3 Each weekly fragment's gridlayout's cell shows the first 10 letters of the first schedule's schedule and if there's more than one schedules on that day, indicate how many more schedules are in that day.</br>
5. So, in order to see the detailed schedule contents daily fragment should be selected. That's why I've implemented so that if the user clicks the cell of the monthly fragment or weekly fragment's gridlayout cell, the corresponding day fragment will show up on the main screen.
6. In daily fragment, aside from the content of schedules, there's a floating action button at the right bottom corner.</br>
6.1 In order to add or delete schedule, just press the floating action button and the main screen will move to ScheduleActivity.</br>
7. In order to add a schedule, fill in the editText and press enter or add button.</br>
7.1 In order to delete the schedule, press the delete button(which looks like waste basket).</br>
7.2 Press the back button to go back to the main screen.</br>
8. All of the schedules will be recorded in SQLite Database.</br>

Bug I need to fix
1. If the user adds the schedule and returns to the main screen by pressing the back button, the tablayout selects the right tab(daily fragment) but the onTouch focus is on the wrong tab(monthly fragment).</br>
--->Let me know if you know the solution to this problem.
