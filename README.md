# Textgram
Textgram is a text-based social media API that allows
users to register, login, post text, comment on posts, follow other users, and view different
types of posts and comments. The API will be used by a front-end application, such as a single-page app.

## Tech Stack

*    Web Framework: Javalin
*    Database: PostgreSQL 16.0
*    Programming Language: Java 17
*	 Build Tool: Maven
*    IDE: IntelliJ
*    Testing: JUnit5
*    Architecture: Hexagonal

## Features
1. Registration  
A user registers using the following:
    *  a valid email address as username
    *  a password that contains at least 1 lowercase, 1 uppercase, 1 number, 
   1 symbol from !@#$%^&* and is 8 characters long.
    * a role from the following: Free, Premium
 
## Use Cases 
**Main Functionalities**   

| Implemented Use Case | Input Port      | Input Model     | Use Case Implementation |
|----------------------|-----------------|-----------------|-------------------------|
| Register             | RegisterUseCase | LoginCommand    | RegisterService         |


| Unimplemented Use Case  | Input Port                     | Input Model                    | Use Case Implementation        |
|-------------------------|--------------------------------|--------------------------------|--------------------------------|
| Login                | LoginUseCase    | RegisterCommand | LoginService            |
| Post                    | PostUseCase                    | PostCommand                    | PostService                    |
| Comment                 | CommentUseCase                 | CommentCommand                 | CommentService                 |
| SearchUser              | SearchUserUseCase              | SearchUserCommand              | SearchUserService              |
| Follow                  | FollowUseCase                  | FollowCommand                  | FollowService                  |
| AddFollower             | AddFollowerUseCase             | AddFollowerCommand             | AddFollowerService             |
| RemoveFollower          | RemoveFollowerUseCase          | RemoveFollowerCommand          | RemoveFollowerService          |
| CreateShareablePostLink | CreateShareablePostLinkUseCase | CreateShareablePostLinkCommand | CreateShareablePostLinkService |
<br>

**Read-Only/Queries/Ingoing Ports** 

| Implemented Use Case | Input Port | Use Case Implementation |
|----------------------|------------|-------------------------|
| -                    | -          | -                       |


| Unimplemented Use Case | Input Port        | Use Case Implementation |
|------------------------|-------------------|-------------------------|
| ViewPosts              | GetPostsQuery     | GetPostsService         |
| ViewComments           | GetCommentsQuery  | GetCommentsService      |  
| ViewFollowers          | GetFollowersQuery | GetFollowersService     |  
| ViewFollowing          | GetFollowingQuery | GetFollowingService     |  
<br>

**Outgoing Ports**

| Implemented Use Case | Output Port | Use Case Implementation(adapter.out.persistence) |
|----------------------|-------------|--------------------------------------------------|
| -                    | -           | -                                                |

| Unimplemented Use Case | Output Port        | Use Case Implementation(adapter.out.persistence) |
|------------------------|--------------------|--------------------------------------------------|
| StoreUser              | StoreUserPort      | UserPersistenceAdapter                           |
| LoadUser               | LoadUserPort       | UserPersistenceAdapter                           |
| StorePost              | StorePostPort      | PostPersistenceAdapter                           |  
| LoadPosts              | LoadPostsPort      | PostPersistenceAdapter                           |  
| StoreComment           | StoreCommentPort   | CommentPersistenceAdapter                        |  
| LoadComments           | LoadCommentsPort   | CommentPersistenceAdapter                        |  
| StoreFollowing         | StoreFollowingPort | FollowingPersistenceAdapter                      |  
| LoadFollowing          | LoadFollowingPort  | FollowingPersistenceAdapter                      |  
| StoreFollower          | StoreFollowerPort  | FollowerPersistenceAdapter                       |  
| LoadFollowers          | LoadFollowersPort  | FollowerPersistenceAdapter                       |  

## Constraints
1. Support up to 500 active users
2. Registration
   1. The username is the email
   2. Users Roles: Free and Premium
3. Posts and Comments 
   1. Free users can post up to 1000 characters and Premium can post up to 3000 characters.
   2. Free users can comment up to 5 times per post and Premium comment unlimited times.