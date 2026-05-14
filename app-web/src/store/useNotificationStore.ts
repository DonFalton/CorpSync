import { create } from 'zustand';
import { persist } from 'zustand/middleware';

export interface NotificationItem {
  id: string;
  titulo: string;
  mensaje: string;
  ticket_id: number;
  fecha: string;
  leida: boolean;
}

interface NotificationStore {
  notificaciones: NotificationItem[];
  unreadCount: number;
  addNotification: (notification: Omit<NotificationItem, 'id' | 'fecha' | 'leida'>) => void;
  markAllAsRead: () => void;
  clearAll: () => void;
}

export const useNotificationStore = create<NotificationStore>()(
  persist(
    (set) => ({
      notificaciones: [],
      unreadCount: 0,
      
      addNotification: (notif) => set((state) => {
        const newNotif: NotificationItem = {
          ...notif,
          id: crypto.randomUUID(),
          fecha: new Date().toISOString(),
          leida: false,
        };
        
        const newNotificaciones = [newNotif, ...state.notificaciones].slice(0, 10);
        
        return {
          notificaciones: newNotificaciones,
          unreadCount: state.unreadCount + 1,
        };
      }),
      
      markAllAsRead: () => set((state) => ({
        unreadCount: 0,
        notificaciones: state.notificaciones.map(n => ({ ...n, leida: true }))
      })),
      
      clearAll: () => set({
        notificaciones: [],
        unreadCount: 0
      })
    }),
    {
      name: 'corpsync-notifications',
    }
  )
);
