import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export interface NotificationItem {
  id: number;
  titulo: string;
  mensaje: string;
  ticket_id: number | null;
  tipo?: string;
  fecha: string;
  leida: boolean;
}

interface NotificationStore {
  notificaciones: NotificationItem[];
  unreadCount: number;
  addNotification: (notification: NotificationItem) => void;
  markAllAsRead: () => void;
  clearNotifications: () => void;
}

export const useNotificationStore = create<NotificationStore>()(
  persist(
    (set) => ({
      notificaciones: [],
      unreadCount: 0,
      
      addNotification: (notif) => set((state) => {
        // Strict deduplication by ID
        const isDuplicate = state.notificaciones.some(n => n.id === notif.id);

        if (isDuplicate) {
          return state; // Do nothing if it already exists
        }

        const newNotificaciones = [notif, ...state.notificaciones].slice(0, 10);
        
        return {
          notificaciones: newNotificaciones,
          unreadCount: state.unreadCount + 1,
        };
      }),
      
      markAllAsRead: () => set((state) => ({
        unreadCount: 0,
        notificaciones: state.notificaciones.map(n => ({ ...n, leida: true }))
      })),
      
      clearNotifications: () => set({
        notificaciones: [],
        unreadCount: 0
      })
    }),
    {
      name: 'corpsync-notifications',
    }
  )
);
