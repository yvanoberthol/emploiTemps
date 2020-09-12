(function() {
      'use strict';
      angular
          .module('myApp')
          .factory('EmploiTemps', EmploiTemps);

      EmploiTemps.$inject = ['$http'];

      function EmploiTemps ($http) {
           return{
              query: function(){
                    return $http.get("/api/emploitemps/classe");
              },
              getByTeacherDay: function(classe,teacherId,jour){
                        return $http.get("/api/emploitemps/classe/"+classe+"/"+teacherId+"/"+jour);
              },
               getTeachers: function() {
                    return $http.get('/api/teachers');
              },
               getPromos: function() {
                   return $http.get('/api/promos');
               },
               saveSeance: function (jour, coursId, enabled) {

                  var seanceDto =
                      {
                          jour: jour,
                          idCours: coursId,
                          enabled: enabled
                      };
                   return $http.post('/api/seances/save',seanceDto);
               },
               saveLecon: function (lecon) {
                   return $http.post('/api/cours/save',lecon);
               },
               deleteLecon: function (idCours) {
                   return $http.delete('/api/cours/delete/'+idCours);
               }
           };
      }
})();