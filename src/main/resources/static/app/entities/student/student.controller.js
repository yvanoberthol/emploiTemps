(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('StudentController', StudentController);

    StudentController.$inject = ['$rootScope', '$scope', '$stateParams', 'session', '$state', '$http',
                                'Student', '$filter', 'NgTableParams', 'Promo'];

    function StudentController ($rootScope, $scope, $stateParams, session, $state, $http,
                                Student, $filter, NgTableParams, Promo) {
        var vm = this;
        vm.loading = true;

        var patternDateTime = "YYYY-MM-DD HH:mm";
        var patternDate = "YYYY-MM-DD";
        vm.name = "";
        vm.promoId = 0;

        Promo.getByAnnee(session.getAnneeId())
            .then(function(response){
                vm.promos = response.data;
        })

        var initialParams = {
            count: 10, // initial page size
            sorting: { 'lastName': "asc" }
        };

        vm.tableParams = new NgTableParams(initialParams, {
            counts: [],
            paginationMaxBlocks: 10,
            paginationMinBlocks: 2,

            getData: function(params) {
                if(params.sorting()){
                    for(var sortBy in params.sorting()){
                        vm.sortBy = sortBy;
                        vm.direction = params.sorting()[sortBy];
                    }
                }

                if(!vm.promoId)
                vm.promoId = 0;
                return Student.getByAnnee(params.page()-1, params.count(), vm.sortBy, vm.direction, session.getAnneeId(), vm.name, vm.promoId)
                .then(function(response){
                    params.total(response.data.totalElements);
                    vm.students = response.data.content;
                    vm.loading = false;
                    return vm.students;
                })
            }
        });
    }
})();

