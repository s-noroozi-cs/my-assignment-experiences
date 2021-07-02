# Interview

This is a provided frontend application needed as part of the RIPE NCC interview assignment.

# Instructions

1. Implement the Java backend application that provides the endpoint described below. 
2. The current endpoint allows a member to have one contact information attached to it. It should be possible 
for a member to have multiple contacts. You need to modify the applications (both the frontend and backend) to allow 
a member to have multiple contacts. 

3. Implement the disabled test in `memberAddComponentSpec.js` file 

# Overview of endpoints

The frontend application expects the backend service to be exposed via port `8080` 
(see MemberResource.js for where this is hardcoded)

This frontend application expects the following endpoints:

## To add a member:

POST /members

``` 
{
     name: "given member name",
     contact: {
        name: "given contact name",
        email: "given contact email",
        phone: "given contact phone"
      }
 }
```

This endpoint should return the result in the following format
```
{
     id: "the server side generated identifier",
     name: "given member name",
     contact: {
        name: "given contact name",
        email: "given contact email",
        phone: "given contact phone"
      }
 }
```

## To view the details of a member

GET /members/:id

This endpoint should return the member as follows:

```
{
     id: "the server side generated identifier",
     name: "given member name",
     contact: {
        name: "given contact name",
        email: "given contact email",
        phone: "given contact phone"
      }
 }
```

## To get a list of all members

GET /members

This endpoint should return the list of members as follows:

```
[{
     id: "the server side generated identifier",
     name: "given member name",
     contact: {
        name: "given contact name",
        email: "given contact email",
        phone: "given contact phone"
      }
 },
 {
      id: "the server side generated identifier",
      name: "another given member name",
      contact: {
         name: "another given contact name",
         email: "another given contact email",
         phone: "another given contact phone"
       }
  }
 ]
```

## Running the application in Development

You will have to first do `npm install` and `bower install` to download the dependencies.

Run `grunt serve` in development to have a quick edit, refresh cycle.

## Testing

Running `grunt test` will run the unit tests with karma.

## Build

Run `grunt build` and serve the content of the generated `dist` folder via a web server.