(function() {
    'use strict';

    angular
        .module('myApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('student', {
            parent: 'app',
            url: '/students',
            data: {
                authorities: ['ADMIN', 'USER'],
                pageTitle: 'myApp.student.home.title'
            },
            templateUrl: 'app/entities/student/students.html',
            controller: 'StudentController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('student');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('student-detail', {
            parent: 'app',
            url: '/student/{studentId}',
            params : { studentId: null },
            data: {
                authorities: ['ADMIN'],
                pageTitle: 'myApp.student.detail.title'
            },
            templateUrl: 'app/entities/student/student-detail.html',
            controller: 'StudentDetailController',
            controllerAs: 'vm',
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('student');
                    $translatePartialLoader.addPart('product');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Student', 'session',  function($stateParams, Student, session) {
                     var student = {};
                     return Student.getOneByAnnee($stateParams.studentId, session.getAnneeId())
                     .then(function(response){
                        student = response.data;
                        return response.data;
                    })
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'student',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('student-detail.edit', {
            parent: 'student-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student/student-dialog.html',
                    controller: 'StudentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'md',
                    resolve: {
                        entity: ['Student',  function(Student) {
                             var student = {};
                              return Student.get($stateParams.studentId)
                              .then(function(response){
                                 return response.data;
                             })
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('student.edit', {
            parent: 'student',
            url: '/{studentId}/edit',
            params : { studentId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student/student-dialog.html',
                    controller: 'StudentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Student',  function(Student) {
                                console.log("in state")
                             return Student.get($stateParams.studentId)
                             .then(function(response){
                                console.log(response)
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('student', null, { reload: 'student' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('student.delete', {
            parent: 'student',
            url: '/{studentId}/delete',
            params : { studentId: null },
            data: {
                authorities: ['ADMIN', 'USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/student/student-delete-dialog.html',
                    controller: 'StudentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Student',  function(Student) {
                            console.log($stateParams.studentId)
                             return Student.get($stateParams.studentId)
                             .then(function(response){
                                return response.data;
                            })
                        }]
                    }
                }).result.then(function() {
                    $state.go('student', null, { reload: 'student' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }
})();
