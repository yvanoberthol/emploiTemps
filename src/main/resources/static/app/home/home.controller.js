(function() {
    'use strict';

    var app = angular.module('myApp');

    app.controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', '$state', '$window', 'auth', 'session', 'Annee'];

    function HomeController ($scope, $state, $window, auth, session, Annee) {
        var vm = this;

        Annee.get(session.getAnneeId()).then(function(response){
            vm.annee = response.data;
        })
    };

}) ();


