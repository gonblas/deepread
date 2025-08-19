import React from "react";
import { Badge } from "../ui/badge";


export function CardBadge({
  icon,
  text,
  className = "",
  variant = "default",
}: {
  icon?: React.ComponentType<React.SVGProps<SVGSVGElement>>;
  text: string;
  className?: string;
  variant?: "default" | "secondary" | "destructive";
}) {
  return (
    <Badge
      variant={variant}
      className={`text-xs font-medium ${className}`}
    >
      {icon && (
        <span className="mr-1">
          {React.createElement(icon, { className: "w-3 h-3" })}
        </span>
      )}
      {text}
    </Badge>
  );
}
