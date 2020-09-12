(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('Seance', Seance);

      Seance.$inject = ['$http'];

      function Seance ($http) {
           return{
              query: function(){
                    return $http.get("/api/seances/");
              },
              getSeancesByTeacher: function(idTeacher,debut,fin){
                        return $http.get("/api/seances-by-teacher/"+idTeacher+"/"+debut+"/"+fin);
              }
           };
      }
})();