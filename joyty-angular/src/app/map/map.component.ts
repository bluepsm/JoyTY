import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { GoogleMap, GoogleMapsModule } from '@angular/google-maps';
import { PlaceSearchResult } from '../shared/place-auto-complete/place-auto-complete.component';

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements OnInit {
  @ViewChild("map", { static: true })
  map!: GoogleMap;
  @Input() place: PlaceSearchResult | undefined
  @Input() mapOptions: google.maps.MapOptions = {}
  @Input() lat: number = 13.75674949725114
  @Input() lng: number = 100.50185329412409

  markerPosition!: google.maps.LatLng

  constructor() {}

  ngOnInit(): void {
    this.mapOptions.center = {lat: this.lat, lng: this.lng}
  }

  ngOnChanges() {
    //Called before any other lifecycle hook. Use it to inject dependencies, but avoid any serious work here.
    //Add '${implements OnChanges}' to the class.
    const place = this.place?.location
    this.markerPosition = place!
    this.map.panTo(place!)
  }
}
