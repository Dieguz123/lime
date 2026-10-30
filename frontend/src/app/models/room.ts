import {Provider} from './provider';
import {User} from './user';

export interface Room {
  name: string;
  inviteCode: string;
  creator: User;
  partner: User | null;
  providers: Provider[];
}
