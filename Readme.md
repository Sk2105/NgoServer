# Ngo Server

Ngo Server is a Spring Boot application that provides a RESTful API for managing Ngo.


## Use Tech

- Java
- Spring Boot
- Hibernet/Spring Data Jpa
- Maven
- PostgreSQL
- JWT
- Lombok


# API Endpoints



## Auth Endpoints

### Register User (Permit All)

Register a new user.

```bash
POST /auth/register
```

Body
```json
{
    "username": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123",
    "role": "ADMIN", // ADMIN,VOLUNTEER,DONAR,
    "phoneNumber": "1234567890"
}
```

| Parameter   | Type   | Description                                                                                                                                                              |
|-------------|--------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| username    | string | User's username. The username must be unique and not empty.                                                                                                              |
| email       | string | User's email. The email must be unique and not empty.                                                                                                                    |
| password    | string | User's password. The password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character. |
| role        | string | User's role. The role must be one of the following: ADMIN, VOLUNTEER, DONOR.                                                                                             |
| phoneNumber | string | User's phone number. The phone number must be a valid phone number.                                                                                                      |

Response Success
```json
{
    "message": "User Registered",
    "status": 201
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "User Already Exists",
    "status": 409
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

Found Empty Element Exception
```json
{
    "message": "Empty Element",
    "status": 400
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

### Login user (Permit All)

Login user with its email and password.

```bash
POST /auth/login
```

Body
```json
{
    "email": "john.doe@example.com",
    "password": "password123"
}
```

| Parameter | Type   | Description     |
|-----------|--------|-----------------|
| email     | string | User's email    |
| password  | string | User's password |

Response Success
```json
{
    "token": "JWT-Token"
    "status": 200
}
```

| Parameter | Type   | Description                                                                                                                                                                                                                                                                            |
|-----------|--------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| token     | string | A JSON Web Token (JWT) which will be used to authenticate the user in future requests. The JWT is signed with a secret key and contains the user's email and other relevant information. For more information about JWT, please refer to the [official documentation](https://jwt.io). |
| status    | number | The HTTP status code of the response. In this case, it should be 200, which means the request was successful.                                                                                                                                                                          |

Response Error
```json
{
    "message": "User Not Found"
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

### Change Password (Authenticated User)

Change user password.

```bash
PUT /auth/change-password
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Body
```json
{
    "password": "password123",
    "newPassword": "newPassword123"
}
```

| Parameter   | Type   | Description             |
|-------------|--------|-------------------------|
| password    | string | User's current password |
| newPassword | string | User's new password     |

Response Success
```json
{
    "message": "Password Changed Successfully"
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Invalid Password"
    "status": 401
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

### Get User (Authenticated User)

Get user details.

```bash
GET /auth/user
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success
```json
{
    "id": 12,
    "username": "Sachink",
    "email": "skb@gmail.com",
    "phoneNumber": "123456789",
    "role": "ADMIN",
    "createdAt": "2025-03-24T23:38:37.481545"
}

```

| Parameter   | Type   | Description                                                            |
|-------------|--------|------------------------------------------------------------------------|
| id          | number | User's ID                                                              |
| username    | string | User's username                                                        |
| email       | string | User's email                                                           |
| phoneNumber | string | User's phone number                                                    |
| role        | string | User's role (ADMIN, VOLUNTEER, DONOR)                                  |
| createdAt   | string | User's creation date in ISO 8601 format e.g. 2025-03-22T10:55:17.25863 |

Response Error
```json
{
    "message": "User Not Found"
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Get All User (Admin Only)

Returns the all users details 

```bash
GET /auth/all
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success

```json
[
    {
        "id": 12,
        "username": "Sachink",
        "email": "skb@gmail.com",
        "phoneNumber": "123456789",
        "role": "ADMIN",
        "createdAt": "2025-03-22T10:55:17.25863"
    },
    {
        "id": 13,
        "username": "Sachink",
        "email": "skb@gmail.com",
        "phoneNumber": "123456789",
        "role": "ADMIN",
        "createdAt": "2025-03-22T10:55:17.25863"
    }
]
```

| Parameter   | Type   | Description                                                            |
|-------------|--------|------------------------------------------------------------------------|
| id          | number | User's ID                                                              |
| username    | string | User's username                                                        |
| email       | string | User's email                                                           |
| phoneNumber | string | User's phone number                                                    |
| role        | string | User's role (ADMIN, VOLUNTEER, DONOR)                                  |
| createdAt   | string | User's creation date in ISO 8601 format e.g. 2025-03-22T10:55:17.25863 |

Response Error
```json
{
    "message": "Access Denied",
    "status": 403
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Get User By Id (Admin Only)

Return the User by it's id 

```bash
GET /auth/all/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success
```json
{
    "id": 12,
    "username": "Sachink",
    "email": "skb@gmail.com",
    "phoneNumber": "123456789",
    "role": "ADMIN",
    "createdAt": "2025-03-22T10:55:17.25863"
}
```

| Parameter   | Type   | Description                                                            |
|-------------|--------|------------------------------------------------------------------------|
| id          | number | User's ID                                                              |
| username    | string | User's username                                                        |
| email       | string | User's email                                                           |
| phoneNumber | string | User's phone number                                                    |
| role        | string | User's role (ADMIN, VOLUNTEER, DONOR)                                  |
| createdAt   | string | User's creation date in ISO 8601 format e.g. 2025-03-22T10:55:17.25863 |

Response Error
```json
{
    "message": "User Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Update User by Id (Admin Only)

Update User Information by it's id 

```bash
PUT /auth/all/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |


Body
```json
{
    "username": "Sachink",
    "email": "skb@gmail.com",
    "phoneNumber": "123456789",
    "role": "ADMIN"
}
```

| Parameter   | Type   | Description                           |
|-------------|--------|---------------------------------------|
| username    | string | User's username                       |
| email       | string | User's email                          |
| phoneNumber | string | User's phone number                   |
| role        | string | User's role (ADMIN, VOLUNTEER, DONOR) |

Response Success
```json
{
    "message": "User Updated Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "User Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |




## Campaign Endpoints


### Get All Campaign (Permit All)

Return all campaigns.

```bash
GET /campaigns
```


Response Success
```json
 [
    {
        "id": 2,
        "title": "Road Sefty",
        "description": "this is description",
        "goalAmount": 50000.0,
        "createdAt": "2025-03-21T21:18:02.740897",
        "collectedAmount": 20000.0,
        "status": "ACTIVE"
    },
    {
        "id": 3,
        "title": "Child Education",
        "description": "this is description",
        "goalAmount": 50000.0,
        "createdAt": "2025-03-21T21:18:02.740897",
        "collectedAmount": 20000.0,
        "status": "ACTIVE"
    }
]

```

| Parameter       | Type   | Description                                                                                       |
|-----------------|--------|---------------------------------------------------------------------------------------------------|
| id              | number | Unique identifier of the campaign                                                                 |
| title           | string | Title of the campaign                                                                             |
| description     | string | Description of the campaign                                                                       |
| goalAmount      | number | The amount of money the campaign is trying to raise                                               |
| createdAt       | string | The date and time when the campaign was created in ISO 8601 format e.g. 2025-03-22T10:55:17.25863 |
| collectedAmount | number | The amount of money the campaign has collected so far                                             |
| status          | string | Status of the campaign (ACTIVE, CANCELLED, COMPLETED)                                             |

Response Error
```json
{
    "message": "User Not Found"
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

### Add new Campaign (Admin Only)

Add a new campaign.

```bash
POST /campaigns
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Body
```json
{
    "title":"Child Education",
    "description":"this is description",
    "goalAmount":50000.0,
    "collectedAmount":20000.0,
    "status":"ACTIVE"
}
```

| Parameter       | Type   | Description                 |
|-----------------|--------|-----------------------------|
| title           | string | Campaign's title            |
| description     | string | Campaign's description      |
| goalAmount      | number | Campaign's goal amount      |
| collectedAmount | number | Campaign's collected amount |
| status          | string | Campaign's status           |





Response Success
```json
{
    "message": "Campaign Added Successfully",
    "status": 201
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Campaign Already Exists",
    "status": 400
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

### Update Campaign (Admin Only)

Update a campaign by its ID.

```bash
PUT /campaigns/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Body
```json
{
    "title":"Child Education",
    "description":"this is description",
    "goalAmount":50000.0,
    "collectedAmount":20000.0,
    "status":"ACTIVE"
}
```

| Parameter       | Type   | Description                 |
|-----------------|--------|-----------------------------|
| title           | string | Campaign's title            |
| description     | string | Campaign's description      |
| goalAmount      | number | Campaign's goal amount      |
| collectedAmount | number | Campaign's collected amount |
| status          | string | Campaign's status           |

Response Success
```json
{
    "message": "Campaign Updated Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Campaign Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Delete Campaign (Admin Only)

Delete a campaign by its ID.

```bash
DELETE /campaigns/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success
```json
{
    "message": "Campaign Deleted Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Campaign Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Campaign By Id (Permit All)

Return a campaign by its ID.

```bash
GET /campaigns/{id}
```


Response Success
```json
{
    "id": 2,
    "title": "Road Sefty",
    "description": "this is description",
    "goalAmount": 50000.0,
    "createdAt": "2025-03-21T21:18:02.740897",
    "collectedAmount": 20000.0,
    "status": "ACTIVE"
}
```

| Parameter       | Type   | Description                 |
|-----------------|--------|-----------------------------|
| id              | number | Campaign's ID               |
| title           | string | Campaign's title            |
| description     | string | Campaign's description      |
| goalAmount      | number | Campaign's goal amount      |
| createdAt       | string | Campaign's creation date    |
| collectedAmount | number | Campaign's collected amount |
| status          | string | Campaign's status           |

Response Error
```json
{
    "message": "Campaign Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |






## Donor Endpoints

### Get All Donors (Permit All)

Return all donors.

```bash
GET /donors
```



Response Success
```json
[
    {
        "id": 3,
        "user": {
            "id": 10,
            "username": "Aarfi",
            "email": "cam105@gmail.com",
            "phoneNumber": "123456789",
            "role": "DONOR",
            "createdAt": "2025-03-22T10:55:17.25863"
        },
        "totalDonation": 0.0,
        "lastDonation": "2025-03-22T10:55:17.25863"
    },
    {
        "id": 4,
        "user": {
            "id": 11,
            "username": "Sachin",
            "email": "sacin105@gmail.com",
            "phoneNumber": "123456789",
            "role": "DONOR",
            "createdAt": "2025-03-22T10:58:16.482636"
        },
        "totalDonation": 0.0,
        "lastDonation": "2025-03-22T10:58:16.482998"
    }
]
```

| Parameter     | Type   | Description                                                                   |
|---------------|--------|-------------------------------------------------------------------------------|
| id            | number | Donor's ID. Unique identifier for the donor.                                  |
| username      | string | Donor's username. The username must be unique and not empty.                  |
| email         | string | Donor's email. The email must be unique and not empty.                        |
| phoneNumber   | string | Donor's phone number. The phone number must be a valid phone number.          |
| role          | string | Donor's role. The role must be one of the following: ADMIN, VOLUNTEER, DONOR. |
| createdAt     | string | Donor's creation date. The date the donor was created.                        |
| totalDonation | number | Donor's total donation. The total amount the donor has donated.               |
| lastDonation  | string | Donor's last donation. The date of the donor's last donation.                 |

Response Error
```json
{
    "message": "Donor Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

### Get Donor By Id (Permit All)

Return a donor by its ID.

```bash
GET /donors/{id}
```


Response Success
```json
{
    "id": 4,
    "user": {
        "id": 11,
        "username": "Sachin",
        "email": "sacin105@gmail.com",
        "phoneNumber": "123456789",
        "role": "DONOR",
        "createdAt": "2025-03-22T10:58:16.482636"
    },
    "totalDonation": 0.0,
    "lastDonation": "2025-03-22T10:58:16.482998",
    "campaigns": []
}
```

| Parameter     | Type   | Description            |
|---------------|--------|------------------------|
| id            | number | Donor's ID             |
| username      | string | Donor's username       |
| email         | string | Donor's email          |
| phoneNumber   | string | Donor's phone number   |
| role          | string | Donor's role           |
| createdAt     | string | Donor's creation date  |
| totalDonation | number | Donor's total donation |
| lastDonation  | string | Donor's last donation  |
| campaigns     | array  | Donor's campaigns      |

Response Error
```json
{
    "message": "Donor Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

## Blog Endpoints

### Get All Blogs (Permit All)

Return all blogs.

```bash
GET /blogs
```

Response Success
```json
[
    {
        "id": 6,
        "title": "Change Socity 6",
        "content": "lorem djfalkjdslkajfjdasj",
        "image": "http://jfajskljla",
        "createdAt": "2025-03-28T10:23:33.581998",
        "updateAt": "2025-03-28T10:23:33.581998",
        "author": {
            "id": 28,
            "username": "Sachin Kumar",
            "email": "sk2105@skg.com",
            "phoneNumber": "9117849946",
            "role": "ADMIN",
            "createdAt": "2025-03-27T10:33:31.413703"
        }
    },
    {
        "id": 7,
        "title": "Change Socity 7",
        "content": "lorem djfalkjdslkajfjdasj",
        "image": "http://jfajskljla",
        "createdAt": "2025-03-28T10:23:33.581998",
        "updateAt": "2025-03-28T10:23:33.581998",
        "author": {
            "id": 28,
            "username": "Sachin Kumar",
            "email": "sk2105@skg.com",
            "phoneNumber": "9117849946",
            "role": "ADMIN",
            "createdAt": "2025-03-27T10:33:31.413703"
        }
    }
]
```

| Parameter          | Type   | Description            |
|--------------------|--------|------------------------|
| id                 | number | Blog's ID              |
| title              | string | Blog's title           |
| content            | string | Blog's content         |
| image              | string | Blog's image           |
| createdAt          | string | Blog's creation date   |
| updatedAt          | string | Blog's update date     |
| author             | object | Blog's author          |
| author.id          | number | Author's ID            |
| author.username    | string | Author's username      |
| author.email       | string | Author's email         |
| author.phoneNumber | string | Author's phone number  |
| author.role        | string | Author's role          |
| author.createdAt   | string | Author's creation date |

Response Error
```json
{
    "message": "Blog Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Add new blog (Admin Only)

Return a new blog.

```bash
POST /blogs
```


Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |


Body
```json
{
    "title": "New Blog",
    "content": "lorem djfalkjdslkajfjdasj",
    "image": "http://jfajskljla"
}
```

| Parameter | Type   | Description    |
|-----------|--------|----------------|
| title     | string | Blog's title   |
| content   | string | Blog's content |
| image     | string | Blog's image   |

Response Success
```json
{
    "message": "Blog Added Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Blog Already Exists",
    "status": 400
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Update Blog (Admin Only)

Return an updated blog.

```bash
PUT /blogs/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

Body
```json
{
    "title": "New Blog",
    "content": "lorem djfalkjdslkajfjdasj",
    "image": "http://jfajskljla"
}
```

| Parameter | Type   | Description    |
|-----------|--------|----------------|
| title     | string | Blog's title   |
| content   | string | Blog's content |
| image     | string | Blog's image   |

Response Success
```json
{
    "message": "Blog Updated Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Blog Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Delete Blog (Admin Only)

Return a deleted blog.

```bash
DELETE /blogs/1
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |


Response Success
```json
{
    "message": "Blog Deleted Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Blog Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Get Blog By Id (Permit All)

Return a blog by its ID.

```bash
GET /blogs/{id}
```

Response Success
```json
{
    "id": 6,
    "title": "Change Socity 6",
    "content": "lorem djfalkjdslkajfjdasj",
    "image": "http://jfajskljla",
    "createdAt": "2025-03-28T10:23:33.581998",
    "updatedAt": "2025-03-28T10:23:33.581998",
    "author": {
        "id": 28,
        "username": "Sachin Kumar",
        "email": "sk2105@skg.com",
        "phoneNumber": "9117849946",
        "role": "ADMIN",
        "createdAt": "2025-03-27T10:33:31.413703"
    },
    "comments": [
        {
            "id": 35,
            "content": "This is gamechanger",
            "createdAt": "2025-03-28T10:24:04.42835",
            "user": {
                "id": 28,
                "username": "Sachin Kumar",
                "email": "sk2105@skg.com",
                "phoneNumber": "9117849946",
                "role": "ADMIN",
                "createdAt": "2025-03-27T10:33:31.413703"
            }
        }
    ]
}
```

| Parameter                 | Type   | Description             |
|---------------------------|--------|-------------------------|
| id                        | number | Blog's ID               |
| title                     | string | Blog's title            |
| content                   | string | Blog's content          |
| image                     | string | Blog's image            |
| createdAt                 | string | Blog's creation date    |
| updatedAt                 | string | Blog's update date      |
| author                    | object | Blog's author           |
| author.id                 | number | Author's ID             |
| author.username           | string | Author's username       |
| author.email              | string | Author's email          |
| author.phoneNumber        | string | Author's phone number   |
| author.role               | string | Author's role           |
| author.createdAt          | string | Author's creation date  |
| comments                  | array  | Blog's comments         |
| comments.id               | number | Comment's ID            |
| comments.content          | string | Comment's content       |
| comments.createdAt        | string | Comment's creation date |
| comments.user             | object | Comment's user          |
| comments.user.id          | number | User's ID               |
| comments.user.username    | string | User's username         |
| comments.user.email       | string | User's email            |
| comments.user.phoneNumber | string | User's phone number     |
| comments.user.role        | string | User's role             |
| comments.user.createdAt   | string | User's creation date    |

Response Error
```json
{
    "message": "Blog Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |








## Event Endpoints

### Get All Events (Permit All)

Return all events.

```bash
GET /events
```

Response Success
```json
{
    "id": 6,
    "title": "Change Socity 6",
    "content": "lorem djfalkjdslkajfjdasj",
    "image": "http://jfajskljla",
    "createdAt": "2025-03-28T10:23:33.581998",
    "updatedAt": "2025-03-28T10:23:33.581998",
    "author": {
        "id": 28,
        "username": "Sachin Kumar",
        "email": "sk2105@skg.com",
        "phoneNumber": "9117849946",
        "role": "ADMIN",
        "createdAt": "2025-03-27T10:33:31.413703"
    },
    "comments": [
        {
            "id": 35,
            "content": "This is gamechanger",
            "createdAt": "2025-03-28T10:24:04.42835",
            "user": {
                "id": 28,
                "username": "Sachin Kumar",
                "email": "sk2105@skg.com",
                "phoneNumber": "9117849946",
                "role": "ADMIN",
                "createdAt": "2025-03-27T10:33:31.413703"
            }
        }
    ]
}
```

| Parameter                 | Type   | Description             |
|---------------------------|--------|-------------------------|
| id                        | number | Event's ID              |
| title                     | string | Event's title           |
| content                   | string | Event's content         |
| image                     | string | Event's image           |
| createdAt                 | string | Event's creation date   |
| updatedAt                 | string | Event's update date     |
| author                    | object | Event's author          |
| author.id                 | number | Author's ID             |
| author.username           | string | Author's username       |
| author.email              | string | Author's email          |
| author.phoneNumber        | string | Author's phone number   |
| author.role               | string | Author's role           |
| author.createdAt          | string | Author's creation date  |
| comments                  | array  | Event's comments        |
| comments.id               | number | Comment's ID            |
| comments.content          | string | Comment's content       |
| comments.createdAt        | string | Comment's creation date |
| comments.user             | object | Comment's user          |
| comments.user.id          | number | User's ID               |
| comments.user.username    | string | User's username         |
| comments.user.email       | string | User's email            |
| comments.user.phoneNumber | string | User's phone number     |
| comments.user.role        | string | User's role             |
| comments.user.createdAt   | string | User's creation date    |

Response Error
```json
{
    "message": "Event Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Get Event by Id (Permit All)

Return an event by its ID.

```bash
GET /events/{id}
```

Response Success
```json
{
    "id": 2,
    "title": "Weekly Webinar",
    "description": "this is description....",
    "image": "http://jjslkjfa",
    "location": "purnea",
    "startDate": "2025-03-23T14:30:00",
    "endDate": "2025-03-23T14:30:00",
    "createdAt": "2025-03-24T22:10:40.728874",
    "updatedAt": "2025-03-25T16:31:07.564634",
    "status": "ACTIVE",
    "volunteers": [
        {
            "id": 6,
            "user": {
                "id": 11,
                "username": "Sachink",
                "email": "sky@gmail.com",
                "phoneNumber": "123456789",
                "role": "VOLUNTEER",
                "createdAt": "2025-03-24T23:09:49.013018"
            },
            "status": "PENDING"
        }
    ]
}
```

| Parameter                   | Type   | Description           |
|-----------------------------|--------|-----------------------|
| id                          | number | Event's ID            |
| title                       | string | Event's title         |
| description                 | string | Event's description   |
| image                       | string | Event's image         |
| location                    | string | Event's location      |
| startDate                   | string | Event's start date    |
| endDate                     | string | Event's end date      |
| createdAt                   | string | Event's creation date |
| updatedAt                   | string | Event's update date   |
| status                      | string | Event's status        |
| volunteers                  | array  | Event's volunteers    |
| volunteers.id               | number | Volunteer's ID        |
| volunteers.user             | object | Volunteer's user      |
| volunteers.user.id          | number | User's ID             |
| volunteers.user.username    | string | User's username       |
| volunteers.user.email       | string | User's email          |
| volunteers.user.phoneNumber | string | User's phone number   |
| volunteers.user.role        | string | User's role           |
| volunteers.user.createdAt   | string | User's creation date  |
| volunteers.status           | string | Volunteer's status    |

Response Error
```json
{
    "message": "Event Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Create New Event (Admin Only)

Create New Event

```bash
POST /events
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Body
```json
{
    "title":"Weekly Webinar",
    "description":"this is description....",
    "location":"purnea",
    "image":"http://jjslkjfa",
    "startDate":"2025-03-23T14:30:00Z",
    "endDate":"2025-03-23T14:30:00Z",
    "status":"ACTIVE"

}
```

| Parameter   | Type   | Description         |
|-------------|--------|---------------------|
| title       | string | Event's title       |
| description | string | Event's description |
| location    | string | Event's location    |
| image       | string | Event's image       |
| startDate   | string | Event's start date  |
| endDate     | string | Event's end date    |
| status      | string | Event's status      |

Response Success
```json
{
    "message": "Event Created Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Event Already Exists",
    "status": 400
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Update Evnet By Id (Admin Only)

Update Event By Id 

```bash
PUT /events/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

Body
```json
{
    "title":"Weekly Webinar",
    "description":"this is description....",
    "location":"purnea",
    "image":"http://jjslkjfa",
    "startDate":"2025-03-23T14:30:00Z",
    "endDate":"2025-03-23T14:30:00Z",
    "status":"ACTIVE"
}
```

Response Success
```json
{
    "message": "Event Updated Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Event Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Delete Event By Id (Admin Only)

Delete events by it's id

```bash
DELETE /events/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success
```json
{
    "message": "Event Deleted Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Event Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Register Volunteer for Event (Volunteer Only)

Register events by event id 

```bash
POST /events/{id}/register
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success
```json
{
    "message": "Event Registered Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Event Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |







## Donation Endpoints

### Get All Donations (Admin Only)

Return all donations.

```bash
GET /donations
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success
```json
[
    {
        "id": 70,
        "amount": 500.0,
        "createdAt": "2025-03-27T18:31:24.167388",
        "donor": {
            "id": 1,
            "user": {
                "id": 1,
                "username": "Sachin",
                "email": "sacin@gmail.com",
                "phoneNumber": "123456789",
                "role": "DONOR",
                "createdAt": "2025-03-23T19:07:47.233012"
            },
            "totalDonation": 3500.0,
            "lastDonation": "2025-03-27T18:45:04.111124"
        },
        "paymentId": null,
        "orderId": "order_QBp7v3rwTDoVjk",
        "signature": null,
        "status": "PENDING"
    },
    {
        "id": 71,
        "amount": 500.0,
        "createdAt": "2025-03-27T18:32:52.111743",
        "donor": {
            "id": 1,
            "user": {
                "id": 1,
                "username": "Sachin",
                "email": "sacin@gmail.com",
                "phoneNumber": "123456789",
                "role": "DONOR",
                "createdAt": "2025-03-23T19:07:47.233012"
            },
            "totalDonation": 3500.0,
            "lastDonation": "2025-03-27T18:45:04.111124"
        },
        "paymentId": null,
        "orderId": "order_QBp9T3kqI9ksDA",
        "signature": null,
        "status": "PENDING"
    }
]
```

| Parameter              | Type   | Description              |
|------------------------|--------|--------------------------|
| id                     | number | Donation's ID            |
| amount                 | number | Donation's amount        |
| createdAt              | string | Donation's creation date |
| updatedAt              | string | Donation's update date   |
| donor                  | object | Donation's donor         |
| donor.id               | number | Donor's ID               |
| donor.user             | object | Donor's user             |
| donor.user.id          | number | User's ID                |
| donor.user.username    | string | User's username          |
| donor.user.email       | string | User's email             |
| donor.user.phoneNumber | string | User's phone number      |
| donor.user.role        | string | User's role              |
| donor.user.createdAt   | string | User's creation date     |
| donor.totalDonation    | number | Donor's total donation   |
| donor.lastDonation     | string | Donor's last donation    |
| paymentId              | string | Payment ID               |
| orderId                | string | Order ID                 |
| signature              | string | Payment signature        |
| status                 | string | Payment status           |

Response Error
```json
{
    "message": "Access Denied",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

### Get Donation by Id (Admin Only)

Get Donation by it's id

```bash
GET /donations/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |


Response Success
```json
{
    "id":70,
    "amount":500.0,
    "createdAt":"2025-03-27T18:31:24.167388",
    "donor":{
        "id":1,
        "user":{
            "id":1,
            "username":"Sachin",
            "email":"sacin@gmail.com",
            "phoneNumber":"123456789",
            "role":"DONOR",
            "createdAt":"2025-03-23T19:07:47.233012"
        },
        "totalDonation":3500.0,
        "lastDonation":"2025-03-27T18:45:04.111124"
    },
    "paymentId":null,
    "orderId":"order_QBp7v3rwTDoVjk",
    "signature":null,
    "status":"PENDING"
}
```

| Parameter              | Type   | Description              |
|------------------------|--------|--------------------------|
| id                     | number | Donation's ID            |
| amount                 | number | Donation's amount        |
| createdAt              | string | Donation's creation date |
| updatedAt              | string | Donation's update date   |
| donor                  | object | Donation's donor         |
| donor.id               | number | Donor's ID               |
| donor.user             | object | Donor's user             |
| donor.user.id          | number | User's ID                |
| donor.user.username    | string | User's username          |
| donor.user.email       | string | User's email             |
| donor.user.phoneNumber | string | User's phone number      |
| donor.user.role        | string | User's role              |
| donor.user.createdAt   | string | User's creation date     |
| donor.totalDonation    | number | Donor's total donation   |
| donor.lastDonation     | string | Donor's last donation    |
| paymentId              | string | Payment ID               |
| orderId                | string | Order ID                 |
| signature              | string | Payment signature        |
| status                 | string | Payment status           |

Response Error
```json
{
    "message": "Donation Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Donation Payment initiation (Donor Only)

Initiate a payment for a donation.

```bash
POST /donations/initiate-donation
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Body
```json
{
    "amount": 500.0,
    "reciept": "<reciept>"
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| amount    | number | Donation amount  |
| reciept   | string | Donation reciept |


Response Success
```json
{
    "amount": 10000,
    "amount_paid": 0,
    "notes": [],
    "created_at": 1743160336,
    "amount_due": 10000,
    "currency": "INR",
    "receipt": "my receipt",
    "id": "order_QCBoTJN8n0pUkj",
    "entity": "order",
    "offer_id": null,
    "attempts": 0,
    "status": "created"
}
```

| Parameter   | Type   | Description     |
|-------------|--------|-----------------|
| amount      | number | Donation amount |
| amount_paid | number | Amount paid     |
| notes       | array  | Notes           |
| created_at  | number | Created at      |
| amount_due  | number | Amount due      |
| currency    | string | Currency        |
| receipt     | string | Receipt         |
| id          | string | ID              |
| entity      | string | Entity          |
| offer_id    | string | Offer ID        |
| attempts    | number | Attempts        |
| status      | string | Status          |

Response Error
```json
{
    "message": "Invalid Request",
    "status": 400
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Verify Donation Payment (Donor Only)

Verify a payment for a donation.

```bash
POST /donations/verify
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |


Body
```json
{
    "paymentId": "<paymentId>",
    "orderId": "<orderId>",
    "signature": "<signature>"
}
```

| Parameter | Type   | Description       |
|-----------|--------|-------------------|
| paymentId | string | Payment ID        |
| orderId   | string | Order ID          |
| signature | string | Payment signature |

Response Success 
```json
{
    "message": "Payment verified successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Message          |
| status    | number | HTTP status code |





Response Error 1
```json
{
    "message": "Invalid Request",
    "status": 400
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


Response Error 2
```json
{
    "message": "Payment not found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

Response Error 3
```json
{
    "message": "Payment Already Verified",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |




## Volunteer Endpoints


### Get All Volunteer (All Authenticated Users)

Return all volunteers

```bash
GET /volunteers
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success
```json
[
    {
        "id": 3,
        "user": {
            "id": 7,
            "username": "Sachink",
            "email": "sk2105y@gmail.com",
            "phoneNumber": "123456789",
            "role": "VOLUNTEER",
            "createdAt": "2025-03-24T21:26:14.576292"
        },
        "status": "PENDING"
    },
    {
        "id": 4,
        "user": {
            "id": 8,
            "username": "Subham karn",
            "email": "subhamkarn132@gmail.com",
            "phoneNumber": "123455678",
            "role": "VOLUNTEER",
            "createdAt": "2025-03-24T15:56:20.001499"
        },
        "status": "PENDING"
    },
    {
        "id": 5,
        "user": {
            "id": 10,
            "username": "Sachink",
            "email": "skg@gmail.com",
            "phoneNumber": "123456789",
            "role": "VOLUNTEER",
            "createdAt": "2025-03-24T22:38:35.04113"
        },
        "status": "PENDING"
    },
    {
        "id": 6,
        "user": {
            "id": 11,
            "username": "Sachink",
            "email": "sky@gmail.com",
            "phoneNumber": "123456789",
            "role": "VOLUNTEER",
            "createdAt": "2025-03-24T23:09:49.013018"
        },
        "status": "ACTIVE"
    }
]
```

| Parameter        | Type   | Description          |
|------------------|--------|----------------------|
| id               | number | Volunteer's ID       |
| user             | object | User details         |
| user.id          | number | User's ID            |
| user.username    | string | User's username      |
| user.email       | string | User's email         |
| user.phoneNumber | string | User's phone number  |
| user.role        | string | User's role          |
| user.createdAt   | string | User's creation date |
| status           | string | Volunteer's status   |


Response Failed
```json
{
    "message": "message",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |

### Get Volunteer By Id (All Authenticated Users)

Return's volunteer Details by it's id

```bash
GET /volunteers/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success
```json
{
    "id": 6,
    "user": {
        "id": 11,
        "username": "Sachink",
        "email": "sky@gmail.com",
        "phoneNumber": "123456789",
        "role": "VOLUNTEER",
        "createdAt": "2025-03-24T23:09:49.013018"
    },
    "status": "ACTIVE",
    "events": [
        {
            "id": 2,
            "title": "Weekly Webinar",
            "description": "this is description....",
            "image": "http://jjslkjfa",
            "location": "purnea",
            "startDate": "2025-03-23T14:30:00",
            "endDate": "2025-03-23T14:30:00",
            "status": "ACTIVE",
            "createdAt": "2025-03-24T22:10:40.728874",
            "updatedAt": "2025-03-25T16:31:07.564634"
        }
    ]
}
```

| Parameter          | Type   | Description           |
|--------------------|--------|-----------------------|
| id                 | number | Volunteer's ID        |
| user               | object | User details          |
| user.id            | number | User's ID             |
| user.username      | string | User's username       |
| user.email         | string | User's email          |
| user.phoneNumber   | string | User's phone number   |
| user.role          | string | User's role           |
| user.createdAt     | string | User's creation date  |
| status             | string | Volunteer's status    |
| events             | array  | Events                |
| events.id          | number | Event's ID            |
| events.title       | string | Event's title         |
| events.description | string | Event's description   |
| events.image       | string | Event's image         |
| events.location    | string | Event's location      |
| events.startDate   | string | Event's start date    |
| events.endDate     | string | Event's end date      |
| events.status      | string | Event's status        |
| events.createdAt   | string | Event's creation date |
| events.updatedAt   | string | Event's update date   |


Response Failed
```json
{
    "message": "message",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Update Volunteer By Id (Admin Only)

To update volunteer status by it's id

```bash
PUT /volunteers/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}
```

| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Body
```json
{
    "status": "ACTIVE" // ACTIVE , PENDING , INACTIVE, BANNED
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| status    | string | Volunteer status   |

Response Success
```json
{
    "message": "Volunteer Updated Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Volunteer Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |


### Delete Volunteer By Id (Admin Only)

```bash
DELETE /volunteers/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```


| Parameter     | Type   | Description  |
|---------------|--------|--------------|
| Authorization | string | Bearer token |

Response Success
```json
{
    "message": "Volunteer Deleted Successfully",
    "status": 200
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Success message  |
| status    | number | HTTP status code |

Response Error
```json
{
    "message": "Volunteer Not Found",
    "status": 404
}
```

| Parameter | Type   | Description      |
|-----------|--------|------------------|
| message   | string | Error message    |
| status    | number | HTTP status code |











