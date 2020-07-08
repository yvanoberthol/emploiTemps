(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Statut', Statut);

      Statut.$inject = ['$http'];

      function Statut ($http) {
           return{
              getAll: function(){
               return $http.get("/api/statuts");
              }
           };
      }
})();