


# Android-Study-Jams

## App name: MyWorkshops

## Problem Statement:
As the Education firm providing courses scales it becomes harder for them to manage course database that they offer and also which students have opted for various courses. It becomes very inconvinient when it comes to datastorage in terms of hard copies or even thousand excel sheets as per courses they offer.
If a student wants to apply to other courses then too it becomes hectic! as he needs to fill all the paperwork just to apply.

## Solution
This app is for supporting macro-level education instututes. These instututes can track students who have opted for varied courses. This app will manage the database and has login system for students. It will make most of the database online and thusly educational institutes at macro-scale wont need to manage all the database over huge pile of hard copies! 
It is also a platform for promotion of new courses! so once education institutes have new courses they can have them here. Interested students can therby directly apply.

# APP

<p align="center"><img src="app/Images/Screenshot%202022-01-09%20at%204.34.39%20PM.png" alt="Whole app screens images" width="1340" height="950"/> 
  
  ### highlighting use of Kotlin features like
- ***Room Database:*** It is used to handle the database of student details and which courses they have enrolled them into.
- ***Kotlin Coroutines:*** We use database and its operations. These opearations are heavy and shoulden't be performed on main theread, as UI operations also need main thread. Doing so will result in laggy UI so we use Coroutines.
- ***Activities:*** We have main activity which hosts different fragment according to use case. Our main activity also has a navigation drawer. And remaining is Login/register activity.
- ***Fragments:*** We have various fragments like 'Available Workshops' showing the currently offered workshops, and 'Applied workshops' showing which workshop student has opted. Remaining are personal details fragments.
- ***Animations:*** the Splashcreen comes with logo transition animation and simple fragment swaping fade in fade out animations, make UI and UX user friendly.


## Future Scope
In future, many education institutes will reach global scale, expanding their learners internationally. It is very important for these Institutes to exist virtually as it enables maximum reach of information and information litracy. This app enables the database operations for the same, hence an intigral part of this system.
  
### Drive link for project and Overview 
  https://drive.google.com/folderview?id=1BzD4raECFqlmtRvMTTU3HkDlK52OoBoH

### ---Done--


