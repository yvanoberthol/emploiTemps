(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('CatUser', CatUser);

      CatUser.$inject = ['$http'];

      function CatUser ($http) {
           return{
              getAll: function(){
               return $http.get("/api/cat-user");
              }
           };
      }
})();