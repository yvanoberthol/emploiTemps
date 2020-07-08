(function (angular) {

  function authService($q, $http, session, USER_ROLES, $state, jwtHelper){

    var isAuthenticated = false;
    var tokenPayload = {};

    loadUserCredentials();

    function loadUserCredentials() {
        if (session.getToken()) {
            useCredentials(session.getToken());
        }
    }

    function useCredentials(token) {
        isAuthenticated = true;
        tokenPayload = jwtHelper.decodeToken(token);
        console.log(tokenPayload);

        // Set the token as header for your requests!
        $http.defaults.headers.common['Authorization'] = 'Bearer ' + token;
    }

    function destroyUserCredentials() {
        isAuthenticated = false;
        $http.defaults.headers.common['Authorization'] = '';
        session.destroy();
    }

    var config = {
        headers : {
            Accept:'text/plain'
        }
    }

    var login = function(credentials) {
        return $http.post('/login', credentials, config)
            .then(function(response){
                console.log(response)
                var jwt = response.data;
                storeAuthenticationToken(jwt);
                return "OK";
            }, function(error){
                return error.data;
        });
    };

    function getToken() {
        return session.getToken();
    }

    function storeAuthenticationToken(jwt) {
        /*
        if (rememberMe) {
            this.$localStorage.store('authenticationToken', jwt);
        } else {
            this.$sessionStorage.store('authenticationToken', jwt);
        }
        */
        session.setToken(jwt);
        useCredentials(jwt);
    }

    var logout = function() {
        destroyUserCredentials();
        $state.go('login');
    };

    var hasRole = function(role){
          var hasRole = false;
          if(isAuthenticated){
              if (tokenPayload.role === role)
                hasRole = true;
          }
        return hasRole;
    };

    var hasRoleInArray = function(roles){
        var hasRole = false;
        if(isAuthenticated){
             roles.forEach(function(role){
                    if(role == tokenPayload.role)
                        hasRole = true;
            })
        }
        return hasRole;
    };

    function getUserId() {
        return tokenPayload.id;
    }

    function getUserId() {
        return tokenPayload.id;
    }

    function getEmail() {
        return tokenPayload.sub;
    }

    function getLastName() {
        return tokenPayload.lastName;
    }

    function getFirstName() {
        return tokenPayload.firstName;
    }

    function getPhone() {
        return tokenPayload.phone;
    }

    function getTypeSchool() {
        return tokenPayload.typeSchool;
    }

    var refreshToken = function() {
        $http.get('/api/refresh-token', config)
        .then(function(response){
            var jwt = response.data;
            storeAuthenticationToken(jwt);
        }, function(error){
            console.log(error);
        })
    };

    return {
        login: login,
        logout: logout,
        refreshToken: refreshToken,
        hasRole: hasRole,
        hasRoleInArray: hasRoleInArray,
        getUserId: getUserId,
        getFirstName: getFirstName,
        getLastName: getLastName,
        getPhone: getPhone,
        getTypeSchool: getTypeSchool,
        isAuthenticated: function() {return isAuthenticated;}
    };
  }

  // Inject dependencies
  authService.$inject = ['$q', '$http', 'session', 'USER_ROLES', '$state', 'jwtHelper'];

  // Export
  angular
    .module('myApp')
    .service('auth', authService);

})(angular);