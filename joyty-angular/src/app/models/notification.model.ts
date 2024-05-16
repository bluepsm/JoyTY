export interface Notification {
    id: bigint;
    type: string;
    entity: string;
    entityId: bigint;
    relatedEntity: string;
    relatedEntityId: bigint;
    createdAt: Date;
    toUser: any;
    fromUser: any;
}