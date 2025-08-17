import React from "react";
import { Button } from "../ui/button";
import { Card, CardContent } from "../ui/card";

export function SearchBox({
  hasActiveFilters, onClearFilters, loading, totalElements, resourcesType = "resources", icon, children
}: {
  hasActiveFilters: boolean;
  onClearFilters: () => void;
  loading: boolean;
  totalElements: number;
  resourcesType?: string;
  icon?: React.ComponentType<any>;
  children?: React.ReactNode;
}) {
  return (
    <Card>
      <CardContent className="pt-6">
        <div className="flex flex-col lg:flex-row gap-4">
          {children}
        </div>

        <div className="flex items-center justify-between mt-4 pt-4 border-t">
          <div className="flex items-center gap-4 text-sm text-muted-foreground">
            <span className="flex items-center gap-1">
              {icon && React.createElement(icon, { className: "size-4" })}
              {loading ? "Loading..." : `${totalElements} ${resourcesType} found`}
            </span>
          </div>

          {hasActiveFilters && !loading && (
            <Button variant="ghost" size="sm" onClick={onClearFilters}>
              Clear filters
            </Button>
          )}
        </div>
      </CardContent>
    </Card>
  );
}
