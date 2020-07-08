(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Sexe', Sexe);

      Sexe.$inject = ['$http'];

      function Sexe ($http) {
           return{
              getAll: function(){
               return $http.get("/api/sexes");
              }
           };
      }
})();