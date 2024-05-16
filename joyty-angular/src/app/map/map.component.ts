import { Component, Input, OnInit, ViewChild } from '@angular/core';
import { GoogleMap } from '@angular/google-maps';
import { PlaceSearchResult } from '../shared/place-auto-complete/place-auto-complete.component';
import { Loader } from "@googlemaps/js-api-loader"

const loader = new Loader({
  apiKey: "AIzaSyBquu5-za-CcYezPdeO-c8GLHcRDDHouCM",
  libraries: ["places"],
  version: "weekly",
})
loader.load().then(async () => {
  //const { Map } = await google.maps.importLibrary("maps") as google.maps.MapsLibrary;
  console.log("Google Maps API loaded")
});

@Component({
  selector: 'app-map',
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements OnInit {
  @ViewChild("map", { static: true })
  map!: GoogleMap;

  @Input() place: PlaceSearchResult | undefined
  @Input() lat: number = 13.75674949725114
  @Input() lng: number = 100.50185329412409

  markerPosition!: google.maps.LatLng

  constructor() {}

  ngOnInit(): void {
    // this.mapOptions.center = {lat: this.lat, lng: this.lng}
    this.initMap()
  }

  ngOnChanges() {
    //Called before any other lifecycle hook. Use it to inject dependencies, but avoid any serious work here.
    //Add '${implements OnChanges}' to the class.
    const place = this.place?.location
    this.markerPosition = place!
    if (this.map.googleMap) {
      this.map.panTo(place!)
    }
  }

  private initMap(): void {
    const mapOptions: google.maps.MapOptions = {
      disableDefaultUI: true,
      center: {lat: this.lat, lng: this.lng},
    }
    this.map.options = mapOptions
    this.map.height = '400px'
    this.map.width = '100%'
    this.map.mapId = '1234567890'
  }
}
