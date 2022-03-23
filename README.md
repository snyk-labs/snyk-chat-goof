# multiroom-chat-goof
A purposely vulnerable, multiroom chat application with Java, Spring, WebSocket on the backend and 
Vue, Vuex, Nuxt.js on the frontend.

## Overview

This project is based off the excellent work of [kojotdev](http://kojotdev.com). Read more about it 
[here](http://kojotdev.com/2019/09/multiroom-chat-with-spring-websocket-nuxt-vue-vuex/).

The project is organized in such a way that it can easily be run locally and be deployed for demonstration.

**NOTE:** The project has known vulnerabilities in it for demonstration purposes. PLEASE do not attempt to use
in any production environment.

![multiroom-chatspring-vue-websocket-live-example](http://kojotdev.com/wp-content/uploads/2019/09/multiroom-chat-live.gif)

## Run - local, separated

Launch the backend with:

```
mvn clean install
mvn spring-boot:run
```

The backend will launch on port `8080`

Launch the frontend with:

```
cd src/frontend
npm install
npm run dev
```

The frontend will launch on port `3000`

You can then browse to: `http://localhost:3000`

Any changes you make to the frontend will trigger an automatic re-build of the frontend.

## Run - local, unified

When you run `mvn clean install`, in addition to building the Java code, it performs the following for the frontend
app:

* builds the nuxt app
* generates a static version of the nuxt app
* copies the static frontend into `src/main/resources/public`

The last step is what enables the frontend app to be served by the spring boot app.

You can run the spring boot app as before:

```
mvn spring-boot:run
```

Now, you can access the frontend at: `http://localhost:8080` and it will automatically connect with backend.

## Run - remote, unified

The combination of the `Procfile` and `system.properties` files makes the app easily deployable to
[Heroku](https://www.heroku.com/).

**NOTE**: In order for the frontend to properly connect to the backend, you'll need to set an environment variable
called: `BASE_URL`.

Do the following to deploy to heroku using the cli too:

```
heroku apps:create my-great-app
heroku config:set BASE_URL=https://my-great-app.herokuapp.com
git push heroku main
```

This will push the code to heroku and run the unified build process. Once complete, you can access the app at the name
you gave it (ex: https://my-great-app.herokuapp.com)