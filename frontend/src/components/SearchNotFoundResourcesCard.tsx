import { Card, CardContent } from "./ui/card";
import React from "react";

export function SearchNotFoundResourcesCard({
  isEmpty,
  resourceType,
  hasActiveFilters,
  noItemsAdvice = "",
  icon,
  callToAction,
}: {
  isEmpty?: boolean;
  resourceType: string;
  hasActiveFilters: boolean;
  noItemsAdvice?: string;
  icon?: React.ComponentType<React.SVGProps<SVGSVGElement>>;
  callToAction?: React.ReactNode;
}) {
  if (!isEmpty) return null;
  return (
    <Card>
      <CardContent className="flex flex-col items-center justify-center py-12">
        {icon && React.createElement(icon, { className: "h-12 w-12 text-muted-foreground mb-4" })}
        <h3 className="text-lg font-semibold mb-2">No {resourceType} found</h3>
        <p className="text-muted-foreground text-center mb-4">
          {hasActiveFilters
            ? `No ${resourceType} match your search criteria. Try different terms or clear your filters.`
            : `No ${resourceType} available. ${noItemsAdvice}`}
        </p>
        {callToAction}
      </CardContent>
    </Card>
  );
}
