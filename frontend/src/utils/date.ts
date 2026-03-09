import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';

dayjs.extend(relativeTime);

export function formatDate(date: string): string {
  return dayjs(date).format('ddd, MMM D · h:mm A');
}

export function fromNow(date: string): string {
  return dayjs(date).fromNow();
}

export function toISOString(date: Date): string {
  return date.toISOString();
}
