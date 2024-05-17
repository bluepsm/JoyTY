export class Post {
    id?: bigint;
    body?: string;
    placeName?: string;
    placeAddress?: string;
    placeLatitude?: number;
    placeLongtitude?: number;
    meetingDatetime?: Date;
    partySize?: number;
    joinner?: number;
    costEstimate?: bigint;
    costShare?: boolean;
    meetingDone?: boolean;
    tags?: any;
    author?: any;
    createdAt?: Date;
    lastUpdated?: Date;
}