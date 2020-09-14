(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('EmploiTempsController', EmploiTempsController);

    EmploiTempsController.$inject = ['$scope', '$stateParams', '$state', '$http',
                                'EmploiTemps','$uibModal'];

    function EmploiTempsController ($scope, $stateParams, $state, $http,
                                EmploiTemps) {
        var vm = this;
        vm.loading = true;
        vm.error = false;
        vm.saveMessage = '';

        vm.emploistemps = [];
        vm.dayWeeks = {};
        vm.creneauHoraires = [];
        vm.matieres = [];
        vm.teachers = [];

        vm.teachers = [];
        $scope.teacherId = '0';

        vm.promos = [];
        $scope.promoId = '1';

        $scope.day = new Date();
        $scope.jourtoday = $scope.day.getDay();
        $scope.dateString = formatDate($scope.day);

        $scope.lecon = {
            jour: 1,
            creneauHoraire: vm.creneauHoraires[0],
            matiere: vm.matieres[0],
            teacher: vm.teachers[0]
        };


        getByTeacherJour($scope.promoId,$scope.teacherId,$scope.dateString);

        EmploiTemps.getTeachers().then(function (response) {
            vm.teachers = response.data.results;
        });

        EmploiTemps.getPromos().then(function (response) {
           vm.promos = response.data;
        });

        vm.searchBy = function (idPromo,idTeacher,jour) {
            if (idPromo === 0){
                vm.error = true;
                return;
            }

            var dateString = formatDate(jour);
            getByTeacherJour(idPromo,idTeacher,dateString);
        };


        vm.saveseance = function (coursId,enabled) {

            var dateString = formatDate($scope.day);
            jQuery.noConflict();
            var element = $('#addPresence'+coursId+' .close');
            element.click();

            EmploiTemps.saveSeance(dateString,coursId,enabled).then(function (response) {
                console.log("enregistrement avec succes");
                getByTeacherJour($scope.promoId,$scope.teacherId,dateString);
            })
        };

        vm.saveLecon = function () {
            var dateString = formatDate($scope.day);
            jQuery.noConflict();
            var element = $('#addLecon .close');
            element.click();

            EmploiTemps.saveLecon($scope.lecon).then(function (response) {
                vm.saveSuccess = response.data.result;
                console.log('lecon enregistrée');
                getByTeacherJour($scope.promoId,$scope.teacherId,dateString);
            });
        };

        vm.deleteLecon = function(idCours){
            var dateString = formatDate($scope.day);
            jQuery.noConflict();
            var element = $('#addPresence'+idCours+' .close');
            element.click();
            EmploiTemps.deleteLecon(idCours).then(function (response) {
                getByTeacherJour($scope.promoId,$scope.teacherId,dateString);
            })
        };

        vm.seancePage = function (teacherId) {
            $state.go('seanceByTeacher',{teacher:teacherId});
        };


        function getByTeacherJour(idPromo,idTeacher,dateString) {
            EmploiTemps.getByTeacherDay(idPromo,idTeacher,dateString).then(function (response) {
                vm.emploistemps = response.data.results.emploiTempsTeacherJours;
                vm.dayWeeks = response.data.results.daysWeek;
                vm.creneauHoraires = response.data.results.creneauHoraires;
                vm.matieres = response.data.results.promoDto.matiereDtos;

                $scope.jourtoday = $scope.day.getDay();
            });
        }

        function formatDate(date) {
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            if (m < 10){
                m = "0"+m;
            }

            if (d < 10){
                d = "0"+d;
            }

            return y+"-"+m+"-"+d;
        }

        function formatDateForFrench(date){
            var y = date.getFullYear();
            var m = date.getMonth()+1;
            var d = date.getDate();
            if (m < 10){
                m = "0"+m;
            }

            if (d < 10){
                d = "0"+d;
            }

            return d+"-"+m+"-"+y;
        }

        $scope.reformatDateForFrench = function(dateString) {
            var date = new Date(dateString);
            return formatDateForFrench(date);
        };

        $scope.parseDate = function (date) {
           return Date.parse(date);
        }
    }
})();

