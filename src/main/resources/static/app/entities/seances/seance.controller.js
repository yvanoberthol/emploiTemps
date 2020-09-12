(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('SeanceController', SeanceController);

    SeanceController.$inject = ['$scope', '$stateParams', '$state', '$http', 'Seance'];

    function SeanceController ($scope, $stateParams, $state, $http, Seance) {
        var vm = this;
        vm.loading = true;
        vm.error = false;
        vm.saveSuccess = false;


        vm.seances = [];
        vm.promos = [];
        vm.arraymap = [];
        vm.montantVacations = 0;


        $scope.debut = new Date();
        $scope.fin = new Date();

        $scope.day = new Date();
        $scope.dateString = formatDate($scope.day);

        getSeancesByTeacher($stateParams.teacher,$scope.dateString,$scope.dateString);

        function getSeancesByTeacher(idTeacher,debut,fin) {
            Seance.getSeancesByTeacher(idTeacher,debut,fin).then(function (response) {
                vm.seances = response.data.seanceDtos;
                vm.promos = response.data.promoDtos;
                vm.arraymap = getSeanceMap(response.data.promoDtos,response.data.seanceDtos);
                console.log(vm.arraymap);

                vm.montantVacations = sumVacation(vm.arraymap);
            })
        }

        function getSeanceMap(promos,seances){
            return promos.map(function (value) {
                var seanceMap = [];
                angular.forEach(seances,function(seance){
                    if (value.classe === seance.classe){
                        seanceMap.push(seance);
                    }
                });

                return {
                    classe: value.classe,
                    seances: seanceMap
                }
            }).filter(function (seanceCours){
                return seanceCours.seances.length > 0
            });
        }

        vm.searchSeanceByTeacher = function () {

            var debutString = formatDate($scope.debut);
            var finString = formatDate($scope.fin);

            getSeancesByTeacher($stateParams.teacher,debutString,finString);
        };

        $scope.numGroups = function (map) {
          var count = 0;
          angular.forEach(map, function () {
              count++;
          });
          return count;
        };

        $scope.formatNumber = function(number) {
            return String(number).replace(/(.)(?=(\d{3})+$)/g,'$1 ');
        };

        function sumVacation(map) {
            var montantTotal = 0;
            map.forEach(function (classe) {
              montantTotal += classe.seances.map(function (seance) {
                    return seance.tauxHoraire;
                }).reduce(function (previousValue,total) {
                    return total + previousValue;
                },0);
            });

            return montantTotal;
        }

        function formatDate(date) {
            var y = date.getFullYear();
            var m = date.getMonth()+1+"";
            var d = date.getDate()+"";
            if (m.length < 2){
                m = "0"+m;
            }

            if (d.length < 2){
                d = "0"+d;
            }

            return y+"-"+m+"-"+d;
        }

        function formatDateForFrench(date){
            var y = date.getFullYear();
            var m = date.getMonth()+1+"";
            var d = date.getDate()+"";
            if (m.length < 2){
                m = "0"+m;
            }

            if (d.length < 2){
                d = "0"+d;
            }

            return d+"-"+m+"-"+y;
        }

        $scope.reformatDateForFrench = function(dateString) {
            var date = new Date(dateString);
            return formatDateForFrench(date);
        }
    }
})();

