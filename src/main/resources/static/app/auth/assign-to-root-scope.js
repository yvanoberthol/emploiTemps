(function (angular) {

  function assignServicesToRootScope($rootScope, auth, session, stateStorage){
    $rootScope.auth = auth;
    $rootScope.session = session;
  }

  // Inject dependencies
  assignServicesToRootScope.$inject = ['$rootScope', 'auth', 'session'];

  // Export
  angular
    .module('myApp')
    .run(assignServicesToRootScope);

})(angular);