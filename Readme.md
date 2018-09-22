# Gitfox

GitFox is your application to manage GitLab projects using an intuitive interface. 
In addition, it is a showcase of the "Clean Architecture" concepts and some useful libraries. 

GitFox brings you the next features: 

* OAuth and custom server authorization.
* Activity stream from all projects.
* Issue and MergeRequest details.
* Markdown support

and much more...

## Code structure

App is constructed according to the Single Activity approach:
* The whole app is contained into the AppActivity
* Every user flow is implemented inside FlowFragment containers
* And every single screen is created in child fragments

### Packages

Here is a description of the most important packages and the logic behind the structure.

**entity** - the main business logic objects

**extension** - utility Kotlin extensions

**model.data** - data sources, such as api, local storage, in-memory cache

**model.repository** - repositories for async work with data sources and mappers for business logic models 

**model.interactor** - mobile app logic divided by features. 
Mostly, the whole app logic is contained on the server, so these classes are quite "thin"

**model.system** - error handling, system helpers, etc

**presentation** - presenters with according view interfaces:
 - presenter handles navigation and only the user input influencing business logic and/or data. 
 In case if there are only view-related changes (animations, changes of view state without touching the business logic, etc)
 these changes should be inside the view implementations and not in the presenter.
 - presenter handles and passes to its View business logic or data changes
 - overall, presenter stays relatively "thin" too

**ui** - view implementation and everything that is bound to views only: animations, data binding, recycler view adapters

## Contribution


Information for contributors is in the [Wiki page](https://gitlab.com/terrakok/gitlab-client/wikis/home)