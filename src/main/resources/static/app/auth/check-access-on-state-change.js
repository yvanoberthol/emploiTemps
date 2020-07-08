(function (angular) {

  function checkAccessOnStateChange($rootScope, auth){

    // Listen for location changes
    // This happens before route or state changes
    $rootScope.$on('$locationChangeStart', function(event, newUrl, oldUrl){
      if(!auth.isLoggedIn()){

        // Redirect to login

        // Prevent location change
        event.preventDefault();
      }
    });

    // Listen for state changes when using ui-router
    $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){

      // Here we simply check if logged in but you can
      // implement more complex logic that inspects the
      // state to see if access is allowed or not
      if(!auth.isAuthenticated()){

        // Redirect to login

        // Prevent state change
        //event.preventDefault();
      }
    });
  }

  // Inject dependencies
  checkAccessOnStateChange.$inject = ['$rootScope', 'auth'];

  // Export
  angular
    .module('myApp')
    .run(checkAccessOnStateChange);

})(angular);