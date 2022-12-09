package com.flyfighter.utils;

public class MathHelper {

    /**两【线段】是否相交
     * @param l1x1 线段1的x1
     * @param l1y1 线段1的y1
     * @param l1x2 线段1的x2
     * @param l1y2 线段1的y2
     * @param l2x1 线段2的x1
     * @param l2y1 线段2的y1
     * @param l2x2 线段2的x2
     * @param l2y2 线段2的y2
     * @return 是否相交
     */
    public static boolean crossLine(double l1x1, double l1y1, double l1x2, double l1y2,
                                    double l2x1, double l2y1, double l2x2, double l2y2) {
        // 快速排斥实验 首先判断两条线段在 x 以及 y 坐标的投影是否有重合。 有一个为真，则代表两线段必不可交。
        if (Math.max(l1x1, l1x2) < Math.min(l2x1, l2x2)
                || Math.max(l1y1, l1y2) < Math.min(l2y1, l2y2)
                || Math.max(l2x1, l2x2) < Math.min(l1x1, l1x2)
                || Math.max(l2y1, l2y2) < Math.min(l1y1, l1y2)) {
            return false;
        }
        // 跨立实验  如果相交则矢量叉积异号或为零，大于零则不相交
        if ((((l1x1 - l2x1) * (l2y2 - l2y1) - (l1y1 - l2y1) * (l2x2 - l2x1))
                * ((l1x2 - l2x1) * (l2y2 - l2y1) - (l1y2 - l2y1) * (l2x2 - l2x1))) > 0
                || (((l2x1 - l1x1) * (l1y2 - l1y1) - (l2y1 - l1y1) * (l1x2 - l1x1))
                * ((l2x2 - l1x1) * (l1y2 - l1y1) - (l2y2 - l1y1) * (l1x2 - l1x1))) > 0) {
            return false;
        }
        return true;
    }
}