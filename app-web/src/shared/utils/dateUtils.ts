export const formatElapsedTime = (dateString: string): string => {
  const diffMs = new Date().getTime() - new Date(dateString).getTime();
  const diffMinutes = Math.floor(diffMs / (1000 * 60));
  const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
  const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

  if (diffMinutes < 60) return `Hace ${Math.max(0, diffMinutes)} min`;
  if (diffHours < 24) return `Hace ${diffHours} ${diffHours === 1 ? 'hora' : 'h'}`;
  return `Hace ${diffDays} ${diffDays === 1 ? 'día' : 'días'}`;
};

export const formatTotalTime = (startString: string, endString: string): string => {
  const diffMs = Math.max(0, new Date(endString).getTime() - new Date(startString).getTime());
  const diffMinutes = Math.floor(diffMs / (1000 * 60));
  const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
  const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

  if (diffMinutes < 60) return `${diffMinutes} min`;
  if (diffHours < 24) return `${diffHours}h ${diffMinutes % 60}m`;
  return `${diffDays}d ${diffHours % 24}h`;
};

export interface TicketSLAStatus {
  text: string;
  badgeClass: string;
  isOverdue: boolean;
}

export const getTicketSLAStatus = (creado_en: string, actualizado_en: string, estado: string): TicketSLAStatus => {
  const isClosed = estado.toLowerCase() === 'resuelto' || estado.toLowerCase() === 'cerrado';
  
  if (isClosed) {
    return {
      text: `Resolución: ${formatTotalTime(creado_en, actualizado_en)}`,
      badgeClass: 'text-gray-600 bg-gray-100 border-gray-200',
      isOverdue: false
    };
  }

  const diffMs = new Date().getTime() - new Date(creado_en).getTime();
  const diffHours = diffMs / (1000 * 60 * 60);

  if (diffHours > 24) {
    return {
      text: formatElapsedTime(creado_en),
      badgeClass: 'text-red-700 bg-red-100 border-red-200',
      isOverdue: true
    };
  } else if (diffHours > 12) {
    return {
      text: formatElapsedTime(creado_en),
      badgeClass: 'text-orange-700 bg-orange-100 border-orange-200',
      isOverdue: false
    };
  } else {
    return {
      text: formatElapsedTime(creado_en),
      badgeClass: 'text-green-700 bg-green-100 border-green-200',
      isOverdue: false
    };
  }
};
