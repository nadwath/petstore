import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
@Component({
  template: `
  <h2>Pets</h2>
  <button (click)="load()">Refresh</button>
  <ul><li *ngFor="let p of pets">{{p.name}} ({{p.status}})</li></ul>`
})
export class PetsListComponent implements OnInit {
  pets:any[]=[];
  constructor(private http: HttpClient){}
  ngOnInit(){ this.load(); }
  load(){ this.http.get<any[]>('/api/pet').subscribe(v => this.pets = v); }
}
