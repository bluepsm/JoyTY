export interface Notification {
    id: bigint;
    type: string;
    entity: string;
    entityId: bigint;
    created_at: Date;
    toUser: any;
    fromUser: any;
}