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
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
