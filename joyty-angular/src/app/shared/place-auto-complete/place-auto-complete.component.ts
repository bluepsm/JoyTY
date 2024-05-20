import { Component, ElementRef, EventEmitter, Input, NgZone, OnInit, Output, Renderer2, ViewChild } from '@angular/core';
import { Loader } from "@googlemaps/js-api-loader"

const loader = new Loader({
  apiKey: "AIzaSyBquu5-za-CcYezPdeO-c8GLHcRDDHouCM",
  libraries: ["places"],
  version: "weekly",
})
loader.load()
// loader.load().then(async () => {
//   //const { Map } = await google.maps.importLibrary("maps") as google.maps.MapsLibrary;
//   console.log("Google Maps API loaded - DEVELOPMENT ONLY")
// });

export interface PlaceSearchResult {
  address: string
  location?: google.maps.LatLng
  name?: string
}

@Component({
  selector: 'app-place-auto-complete',
  templateUrl: './place-auto-complete.component.html',
  styleUrl: './place-auto-complete.component.css'
})
export class PlaceAutoCompleteComponent implements OnInit {
  @ViewChild("inputField")
  inputField!: ElementRef;
  @Input() placeholder = "Enter Place"
  @Input() placeInit?: PlaceSearchResult
  @Output() placeChanged = new EventEmitter<PlaceSearchResult>();

  autoComplete: google.maps.places.Autocomplete | undefined;

  constructor(
    private ngZone: NgZone,
  ) { }

  ngOnInit(): void {    
    
  }

  ngAfterViewInit(): void {
    //Called after ngAfterContentInit when the component's view has been initialized. Applies to components only.
    //Add 'implements AfterViewInit' to the class.
    this.autoComplete = new google.maps.places.Autocomplete(this.inputField.nativeElement)

    this.autoComplete.addListener("place_changed", () => {
      this.ngZone.run(() => {
        const place = this.autoComplete?.getPlace()

        const result: PlaceSearchResult = {
          address: this.inputField.nativeElement.value,
          name: place?.name,
          location: place?.geometry?.location
        }

        this.placeChanged.emit(result);
        //console.log(result)
      })
    })
  }

  ngOnDestroy() {
    if (this.autoComplete) {
      google.maps.event.clearInstanceListeners(this.autoComplete);
    }
  }
}
