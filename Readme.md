# Ngo Server

Ngo Server is a Spring Boot application that provides a RESTful API for managing Ngo.

# API Endpoints

## Auth Endpoints

### Register User

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

| Parameter | Type | Description |
| --- | --- | --- |
| username | string | User's username |
| email | string | User's email |
| password | string | User's password |
| role | string | User's role |
| phoneNumber | string | User's phone number |

Response Success
```json
{
    "message": "User Registered",
    "status": 201
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Success message |
| status | number | HTTP status code |

Response Error
```json
{
    "message": "User Already Exists",
    "status": 409
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |

Found Empty Element Exception
```json
{
    "message": "Empty Element",
    "status": 400
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |

### Login user

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

| Parameter | Type | Description |
| --- | --- | --- |
| email | string | User's email |
| password | string | User's password |

Response Success
```json
{
    "token": "JWT-Token"
    "status": 200
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| token | string | JWT token |
| status | number | HTTP status code |

Response Error
```json
{
    "message": "User Not Found"
    "status": 404
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |

### Change Password

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

| Parameter | Type | Description |
| --- | --- | --- |
| Authorization | string | Bearer token |

Body
```json
{
    "password": "password123",
    "newPassword": "newPassword123"
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| password | string | User's current password |
| newPassword | string | User's new password |

Response Success
```json
{
    "message": "Password Changed Successfully"
    "status": 200
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Success message |
| status | number | HTTP status code |

Response Error
```json
{
    "message": "Invalid Password"
    "status": 401
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |

### Get User

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

| Parameter | Type | Description |
| --- | --- | --- |
| Authorization | string | Bearer token |

Response Success
```json
{
    
    "id": 1,
    "username": "sk2105@gmail.com",
    "email": "sk2105@gmail.com",
    "passwordHash": "$2a$10$e/yNy/TgSsSfA9Alyp.gpul/C9Ndww3D38ox5QSlZymTb4J7pTU5O",
    "phoneNumber": null,
    "role": "ADMIN",
    "createdAt": "2025-03-20T22:47:49.226321",
    "password": "$2a$10$e/yNy/TgSsSfA9Alyp.gpul/C9Ndww3D38ox5QSlZymTb4J7pTU5O",
    "authorities": [
        {
            "authority": "ADMIN"
        }
    ],
    "enabled": true,
    "accountNonLocked": true,
    "accountNonExpired": true,
    "credentialsNonExpired": true

}
```

| Parameter | Type | Description |
| --- | --- | --- |
| id | number | User's ID |
| username | string | User's username |
| email | string | User's email |
| phoneNumber | string | User's phone number |
| role | string | User's role |
| createdAt | string | User's creation date |
| password | string | User's password |
| authorities | array | User's authorities |
| enabled | boolean | User's enabled status |
| accountNonLocked | boolean | User's account non-locked status |
| accountNonExpired | boolean | User's account non-expired status |
| credentialsNonExpired | boolean | User's credentials non-expired status |

Response Error
```json
{
    "message": "User Not Found"
    "status": 404
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |

## Campaign Endpoints


### Get All Campaign

```bash
GET /campaign
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter | Type | Description |
| --- | --- | --- |
| Authorization | string | Bearer token |

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

| Parameter | Type | Description |
| --- | --- | --- |
| id | number | Campaign's ID |
| title | string | Campaign's title |
| description | string | Campaign's description |
| goalAmount | number | Campaign's goal amount |
| createdAt | string | Campaign's creation date |
| collectedAmount | number | Campaign's collected amount |
| status | string | Campaign's status |

Response Error
```json
{
    "message": "User Not Found"
    "status": 404
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |

### Add new Campaign

```bash
POST /campaign
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter | Type | Description |
| --- | --- | --- |
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

| Parameter | Type | Description |
| --- | --- | --- |
| title | string | Campaign's title |
| description | string | Campaign's description |
| goalAmount | number | Campaign's goal amount |
| collectedAmount | number | Campaign's collected amount |
| status | string | Campaign's status |





Response Success
```json
{
    "message": "Campaign Added Successfully",
    "status": 201
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Success message |
| status | number | HTTP status code |

Response Error
```json
{
    "message": "Campaign Already Exists",
    "status": 400
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |

### Update Campaign

```bash
PUT /campaign/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter | Type | Description |
| --- | --- | --- |
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

| Parameter | Type | Description |
| --- | --- | --- |
| title | string | Campaign's title |
| description | string | Campaign's description |
| goalAmount | number | Campaign's goal amount |
| collectedAmount | number | Campaign's collected amount |
| status | string | Campaign's status |

Response Success
```json
{
    "message": "Campaign Updated Successfully",
    "status": 200
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Success message |
| status | number | HTTP status code |

Response Error
```json
{
    "message": "Campaign Not Found",
    "status": 404
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |


### Delete Campaign

```bash
DELETE /campaign/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter | Type | Description |
| --- | --- | --- |
| Authorization | string | Bearer token |

Response Success
```json
{
    "message": "Campaign Deleted Successfully",
    "status": 200
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Success message |
| status | number | HTTP status code |

Response Error
```json
{
    "message": "Campaign Not Found",
    "status": 404
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |


### Campaign By Id

```bash
GET /campaign/{id}
```

Header
```json
{
    "Authorization": "Bearer <token>"
}           
```

| Parameter | Type | Description |
| --- | --- | --- |
| Authorization | string | Bearer token |

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

| Parameter | Type | Description |
| --- | --- | --- |
| id | number | Campaign's ID |
| title | string | Campaign's title |
| description | string | Campaign's description |
| goalAmount | number | Campaign's goal amount |
| createdAt | string | Campaign's creation date |
| collectedAmount | number | Campaign's collected amount |
| status | string | Campaign's status |

Response Error
```json
{
    "message": "Campaign Not Found",
    "status": 404
}
```

| Parameter | Type | Description |
| --- | --- | --- |
| message | string | Error message |
| status | number | HTTP status code |











