'use strict';

/**
 * @ngdoc overview
 * @name memberApp
 * @description
 * # memberApp
 *
 * Main module of the application.
 */
angular
  .module('memberApp', [
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngTable'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        template: '<member-list></member-list>'
      })
      .when('/members', {
        template: '<member-list></member-list>'
      }).when('/members/add', {
      template: '<member-add></member-add>'
      }).when('/members/:id', {
        template: '<member></member>'
      })
  });
