(function() {
    'use strict';

    angular
        .module('myApp')
        .controller('PromoStudentsDialogController', PromoStudentsDialogController);

    PromoStudentsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Promo',
                                            'Student', 'NgTableParams'];

    function PromoStudentsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Promo,
                                        Student, NgTableParams) {
        var vm = this;
        vm.promo = entity;
        console.log(vm.promo);
        vm.loading = true;

        vm.clear = clear;

        function clear () {
            $uibModalInstance.dismiss();
        }

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

                return Student.getByAnnee(params.page()-1, params.count(), vm.sortBy, vm.direction, vm.promo.anneeId, "", vm.promo.id)
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
