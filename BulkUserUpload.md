# spring-security-zitadel

## API to add bulk user to organization

- URL: https://issuerUri.zitadel.cloud/admin/v1/import
- Method: POST
- Authorization: Bearer token
- Request body
- 
```
  {
  "timeout": "10m",
  "data_orgs": {
    "orgs": [
      {
        "orgId": "1041333874632-ExistingOrgId", 
        "humanUsers": [
          {
            "userId": "new UserID or getting from existing sytem",
            "user": {
              "userName": "acuser51",
              "profile": {
                "firstName": "Road",
                "lastName": "Runner",
                "displayName": "Road Runner",
                "preferredLanguage": "de"
              },
              "email": {
                "email": "acuser51@demo.com",
                "isEmailVerified": true
              },
              "hashedPassword": {
                "value": "$2a$14$aPbwhMVJSVrRRW2NoM/5.esSJO6o/EIGzGxWiM5SAEZlGqCsr9DAK",
                "algorithm": "bcrypt"
              }
            }
          },
          {
            "userId": "10413339651852",
            "user": {
              "userName": "acuser52",
              "profile": {
                "firstName": "Road",
                "lastName": "Runner",
                "displayName": "Road Runner",
                "preferredLanguage": "de"
              },
              "email": {
                "email": "acuser52@demo.com",
                "isEmailVerified": true
              },
              "hashedPassword": {
                "value": "$2a$14$aPbwhMVJSVrRRW2NoM/5.esSJO6o/EIGzGxWiM5SAEZlGqCsr9DAK",
                "algorithm": "bcrypt"
              }
            }
          }
        ]
      }
    ]
  }
}
```

- Succesful API response
  - Status 200
  - Response
```
    {
    "errors": [
        {
            "type": "org",
            "id": "1041333914632",
            "message": "ID=ORG-lapo2m Message=Errors.Org.AlreadyExisting"
        }
    ],
    "success": {
        "orgs": [
            {
                "orgId": "1041333974632",
                "humanUserIds": [
                    "10413339121851",
                    "10413339121852"
                ]
            }
        ]
    }
}
```
- Ignore the org error
- How to get Bearer token
  - Inside organization create `Service user` under tab `User -> Service Users -> New
  - After creating user click on the user and from the left panel click on `Personal access token`
  - Use that token as bearer token mentioned above
  - Give user proper permission
    - Click on instance
    - Click on users (top right)
    - For new user click on New
    - For existing user click on role column of that user and mark all check mark
