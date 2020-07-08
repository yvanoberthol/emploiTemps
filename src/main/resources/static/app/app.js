angular
.module('myApp', [
  'ui.router',
  'ngStorage',
  'tmh.dynamicLocale',
  'pascalprecht.translate',
  'ngCookies',
  'ngMaterial',
  'ngAnimate',
  'ui.bootstrap',
  'ui.mask',
  'ngFileUpload',
  'angular-jwt',
  'ngTable',
  'angucomplete-alt',
  'daterangepicker',
  'ui.calendar',
  'textAngular',
  'pdfjsViewer'
])

.config(function($locationProvider) {
      $locationProvider.html5Mode(true);
})

//Possibly unhandled rejection with Angular 1.5.9 #2889
.config(['$qProvider', function ($qProvider) {
      $qProvider.errorOnUnhandledRejections(false);
}])

.run(function ($rootScope, $state, auth, session, AUTH_EVENTS, LoginService) {

    $rootScope.$on('$stateChangeSuccess',function(event, next, nextParams, fromState){
        if(next.name == 'home'
            || next.name == 'je-buy'
            || next.name == 'je-travel'
            || next.name == 'shop'
            || next.name == 'register')
        document.body.scrollTop = document.documentElement.scrollTop = 0;
    });

    $rootScope.$on('$stateChangeStart', function (event, next, nextParams, fromState) {

        if ('data' in next && 'authorities' in next.data) {
          var authorities = next.data.authorities;
          if (authorities.length && !auth.hasRoleInArray(authorities)) {
              console.log('not authorized')
              //$state.go($state.current, {}, {reload: true});
              $rootScope.$broadcast(AUTH_EVENTS.notAuthorized);
              event.preventDefault();
          }
        }

        if (!auth.isAuthenticated()) {
          if (next.name !== 'login'
                //&& next.name !== 'home'
                && next.name !== 'register'
                && next.name !== 'activate'
                && next.name !== 'requestReset'
                && next.name !== 'finishReset'
            ) {
            event.preventDefault();
            $state.go('login');
          }
        }
    });

    $rootScope.$on(AUTH_EVENTS.notAuthenticated, function(event) {
        console.log("not authenticated")
        //AuthService.logout();
        //$state.go('login');
    });

    $rootScope.$on(AUTH_EVENTS.notAuthorized, function(event) {
      //AuthService.logout();
      $state.go('403');
    });
})

.config(function($provide) {
    // this demonstrates how to register a new tool and add it to the default toolbar
    $provide.decorator('taOptions', ['taRegisterTool', '$delegate', function(taRegisterTool, taOptions) { // $delegate is the taOptions we are decorating
        /*taOptions.toolbar = [
              ['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],
              ['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'redo', 'undo', 'clear'],
              ['justifyLeft', 'justifyCenter', 'justifyRight', 'indent', 'outdent'],
              ['html','insertLink']
          ];*/

        taOptions.toolbar = [
            ['h2', 'h3', 'h4', 'h5', 'h6', 'p','insertLink'],
            ['ul', 'ol', 'bold', 'italics', 'underline']
        ];
        return taOptions;
    }]);
})

.config(function ($httpProvider) {
  //$httpProvider.interceptors.push('AuthInterceptor');
  $httpProvider.interceptors.push('authExpiredInterceptor');
});

