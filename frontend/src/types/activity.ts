export type ActivityCategory =
  | 'SPORTS'
  | 'MOVIES'
  | 'DINING'
  | 'OUTDOORS'
  | 'GAMING'
  | 'STUDY'
  | 'MUSIC'
  | 'OTHER';

export type ActivityStatus = 'OPEN' | 'FULL' | 'CANCELLED' | 'COMPLETED';

export interface CreatorInfo {
  id: number;
  displayName: string;
}

export interface Activity {
  id: number;
  title: string;
  location: string;
  activityDate: string;
  category: ActivityCategory;
  status: ActivityStatus;
  maxParticipants: number;
  currentParticipants: number;
  creator: CreatorInfo;
  createdAt: string;
}

export interface ParticipantInfo {
  userId: number;
  displayName: string;
  joinedAt: string;
}

export interface ActivityDetail extends Activity {
  description: string | null;
  updatedAt: string;
  participants: ParticipantInfo[];
}
