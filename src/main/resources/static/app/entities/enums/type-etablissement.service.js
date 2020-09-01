(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('TypeEtablissement', TypeEtablissement);

      TypeEtablissement.$inject = ['$http'];

      function TypeEtablissement ($http) {
           return{
              getAll: function(){
               return $http.get("/api/types-etablissement");
              }
           };
      }
})();