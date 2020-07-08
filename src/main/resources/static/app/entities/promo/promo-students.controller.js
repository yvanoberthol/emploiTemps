(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoStudentsController', PromoStudentsController);

    PromoStudentsController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity',
                                        'Student', '$filter', 'NgTableParams'];

    function PromoStudentsController($scope, $rootScope, $stateParams, previousState, entity,
                                        Student, $filter, NgTableParams) {
        var vm = this;
        vm.promo = entity;
        vm.previousState = previousState.name;
        vm.name = "";

        var initialParamsPromos = {
            count: 20, // initial page size
            //sorting: { 'name': "asc" }
        };

        Student.getByPromo(vm.promo.id, vm.name)
            .then(function(response){
                vm.loading = false;
                vm.students = response.data;
                vm.tableParams = new NgTableParams(initialParamsPromos, {
                    counts: [],
                    paginationMaxBlocks: 10,
                    paginationMinBlocks: 2,
                    dataset: vm.students
                });
        })

    }
})();
