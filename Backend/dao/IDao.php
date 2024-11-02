<?php
interface IDao {
function create($o);
function delete($o);
function update($o);
function getAllRappels();
function findById($id);
}