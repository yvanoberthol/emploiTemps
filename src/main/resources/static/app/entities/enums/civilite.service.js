(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Civilite', Civilite);

      Civilite.$inject = ['$http'];

      function Civilite ($http) {
           return{
              getAll: function(){
               return $http.get("/api/civilites");
              }
           };
      }
})();