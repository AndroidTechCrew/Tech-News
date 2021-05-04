# TechNews

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
-    Tech news aggregator that pulls news articles from various news sites.  Users will be able like/comment news articles and share articles to other users.
### App Evaluation

- **Category:**
    - Productivity
- **Mobile:**
    - Mobile is essential for users to able to find out about daily tech news around the world
- **Story:**
    - Creates a network of between tech news and reader. Allows those who are interested in tech news to find others that do so too. 
- **Market:**
    - Tech-people, like CS and IT workers/hobbyists/students
- **Habit:**
    - Tech focused people who want to get the latest tech news in one place
      instead of having to browse multiple sources.
- **Scope:**
    - Allow users to like/favorite an aritcle
    - Allow users to leave a comment about the article
    - Allow users to save articles which can be viewed later
    - Allow users to link other users to a news article 
    - Allow users to search for other people's accounts and see what they liked and commented on

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Home page that has tech news
* Like, comment and share functionality 
* Profile page that shows accounts likes and comments on certain news articles
* Saved news page - shows all saved articles
* Login page
* Tech news content
 

**Optional Nice-to-have Stories**

* settings
* Options to personalize the news that's displayed more
* User can search for keywords to find articles

**COMPLETED USER STORIES**

SPRINT 1
- [X] Home Feed Fragment where users can view articles (currently can't save articles)
- [X] Saved news Fragment where users can see saved articles (currently only manually added through firestore)
- [X] Profile Fragement where users can log out and view current user logged on
- [X] Login Page where users must enter an email and password (Access create account, if one isn't made)
- [X] Create Account Page where users can enter an email and password to create a new account
- [X] Implementation Web views to visit urls of articles
- [X] Integration of Firebase Firestore to pull and save user information

SPRINT 2
- [X] Home Feed Fragment where users can view and save articles (Now can be saved by user)
- [X] Saved news Fragment where users can see saved articles or remove them (Saved articles no longer need to be manually inserted)
- [X] New updated UI to Login and Create Account Pages 
- [X] Users must add first name, last name and username to create an account
- [X] Profile Fragement now allows user access an edit account page and view uploaded profile picture (If one isn't uploaded, displays default image)
- [X] Edit Profile Page where users can change first name, last name and username (Part1)
- [X] Edit Profile Page where users can add profile picture either from their camera gallery or use their device's camera (PART2)
- [X] Integration of Firebase Storage to save and pull user files (Profile picture)

SPRINT 3
- [X] Home Feed Fragment UI updated
- [X] Saved news Fragment UI updated
- [X] Profile Page Fragments UI updated
- [X] User can comment on articles and see their comments and other people's comments
- [X] User can see what they comment and click on comment view article comments on profile

### 2. Screen Archetypes

* Login
* Stream - Home page
* Detail - Short except from the article
* Profile Page 
* Search - User can search for other users

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home Tab
* Profile Tab
* Saved Tab


**Flow Navigation** (Screen to Screen)

* Login Screen/Create account screen (New User)
   * After Login: Home Page
   * More detailed news from home page
   * Profile page
   * Saved Page
* Home Page (Returning User)
   * More detailed news from home page
   * Profile page
   * Saved Page
 

## Wireframes
<img src="https://github.com/AndroidTechCrew/Tech-News/blob/master/MVIMG_20210401_180248.jpg" width=600 hieght=800>

<img src="https://github.com/AndroidTechCrew/Tech-News/blob/master/TechNewsWireframe.png" width=600 hieght=800>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models

Post: Under the assumption you will click on it for the full article.

| Property |  Type | Description   |
| :------: | :---: | :-----------: |
| image    | File  | Image from API|
| publishedDate | DateTime | Time article was published
| author | String | Name of Author |
| publisher | String | Name of publisher |
| briefDescription | String | Brief description of article |

Profile Page
| Property |  Type | Description   |
| :------: | :---: | :-----------: |
| username | String | Username |
| profileImage | Image | User's profile image |

Saved Articles Page
| Property |  Type | Description   |
| :------: | :---: | :-----------: |
| savedArticles | List[Post] | List of post objects that are saved |








### Networking
- Home Screen
    - (Create/POST) save a technews article. 
        - ```java 
                
                Map<String, String> savedNews = new HashMap<>();
                savedNews.put("title", "some title");
                savedNews.put("author", "some author");
                savedNews.put("Description", "some description");
                savedNews.put("url", "some url");
                savedNews.put("imageURL", "some image url");
                savedNews.put("publishedAt", "string of date");
                // maybe add this: savedNews.put("content", "the whole article");
                
                Map<String, Map> savedNewFolder = new HashMap<>();
                
                savedNewFolder.put("news Id given from API", savedNews);
                
                db.collection("users/" + currentUserUID + "/savedNews")
                .add(savedNewsFolder)
                
                /*note that adding to a collection requires onSuccess and
                onFailure listeners*/

- Profile Screen
    - (Read/GET) Get all the user information
        - ```java
            db.Collection("users/" + currentUserUID + "/profile").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
            });
            
- Saved News Screen
    - (Read/GET) Get all the user saved tech news
        - ```java
            db.Collection("users/" + currentUserUID + "/savedNews").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.w(TAG, "Error getting documents.", task.getException());
                }
            }
            });


| HTTP Verb | End Point | Description |
| -------- | -------- | -------- |
| ```GET```    | /everything    | Search through millions of articles from over 75,000 large and small news sources and blogs. This endpoint suits article discovery and analysis.|
| ```GET``` | /top-headlines | This endpoint provides live top and breaking headlines for a country, specific category in a country, single source, or multiple sources. You can also search with keywords. Articles are sorted by the earliest date published first.|
| ```GET``` | /sources | This endpoint returns the subset of news publishers that top headlines (/v2/top-headlines) are available from. It's mainly a convenience endpoint that you can use to keep track of the publishers available on the API, and you can pipe it straight through to your users.|


## First Sprint
<img src="https://github.com/AndroidTechCrew/Tech-News/blob/master/TechNewsFirstSprint.gif" width=400 hieght=600>
<img src="https://github.com/AndroidTechCrew/Tech-News/blob/master/firestoreAccountFull.gif">

## Second Sprint
<img src="https://github.com/AndroidTechCrew/Tech-News/blob/master/sprint2Part1.gif">
<img src="https://github.com/AndroidTechCrew/Tech-News/blob/master/sprint2part2.gif">
<img src="https://github.com/AndroidTechCrew/Tech-News/blob/master/sprint2part3.gif">

