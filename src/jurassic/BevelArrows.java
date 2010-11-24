/*
 * Copyright (c) 2010-2011 Jurassic Michele Marcon. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Jurassic Michele Marcon nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jurassic;

/**
 *
 * @author mmarcon
 */
import java.awt.*;
import java.awt.geom.*;

public class BevelArrows {

    public static void drawDN(Graphics2D g, int startX, int startY, int endX, int endY) {
        // as we're filling rather than stroking, control point is at the apex,

        float arrowRatio = 0.5f;
        float arrowLength = 9.0f;

        BasicStroke stroke = (BasicStroke) g.getStroke();

        //float endX = 350.0f;

        float veeY = endY - stroke.getLineWidth() * 0.5f / arrowRatio;

        // vee
        Path2D.Float path = new Path2D.Float();

        float waisting = 0.5f;

        float waistY = endY - arrowLength * 0.5f;
        float waistX = arrowRatio * arrowLength * 0.5f * waisting;
        float arrowWidth = arrowRatio * arrowLength;

        path.moveTo(endX - arrowWidth, veeY - arrowLength);
        path.quadTo(endX - waistX, waistY, endX, endY);
        path.quadTo(endX + waistX, waistY, endX + arrowWidth, veeY - arrowLength);

        // end of arrow is pinched in
        path.lineTo(endX, veeY - arrowLength * 0.75f);
        path.lineTo(endX - arrowWidth, veeY - arrowLength);

        g.fill(path);

        // move stem back a bit
        if (startX != endX) {
            g.draw(new Line2D.Float(startX, startY, endX, startY));
            g.draw(new Line2D.Float(endX, startY, endX, veeY - arrowLength * 0.5f));
        } else {
            g.draw(new Line2D.Float(startX, startY, endX, veeY - arrowLength * 0.5f));
        }
    }

    public static void drawUP(Graphics2D g, int startX, int startY, int endX, int endY) {

        // as we're filling rather than stroking, control point is at the apex,

        float arrowRatio = 0.5f;
        float arrowLength = 9.0f;

        BasicStroke stroke = (BasicStroke) g.getStroke();

        //float endX = 350.0f;

        float veeY = endY + stroke.getLineWidth() * 0.5f / arrowRatio;

        // vee
        Path2D.Float path = new Path2D.Float();

        float waisting = 0.5f;

        float waistY = endY + arrowLength * 0.5f;
        float waistX = arrowRatio * arrowLength * 0.5f * waisting;
        float arrowWidth = arrowRatio * arrowLength;

        path.moveTo(endX - arrowWidth, veeY + arrowLength);
        path.quadTo(endX - waistX, waistY, endX, endY);
        path.quadTo(endX + waistX, waistY, endX + arrowWidth, veeY + arrowLength);

        // end of arrow is pinched in
        path.lineTo(endX, veeY + arrowLength * 0.75f);
        path.lineTo(endX - arrowWidth, veeY + arrowLength);

        g.fill(path);

        // move stem back a bit
        if (startX != endX) {
            g.draw(new Line2D.Float(startX, startY, endX, startY));
            g.draw(new Line2D.Float(endX, startY, endX, veeY + arrowLength * 0.5f));
        } else {
            g.draw(new Line2D.Float(startX, startY, endX, veeY + arrowLength * 0.5f));
        }
    }
    // to draw a nice curved arrow, fill a V shape rather than stroking it with lines

    public static void drawSX(Graphics2D g, int startX, int startY, int endX, int endY) {

        // as we're filling rather than stroking, control point is at the apex,

        float arrowRatio = 0.5f;
        float arrowLength = 9.0f;

        BasicStroke stroke = (BasicStroke) g.getStroke();

        //float endX = 350.0f;

        float veeX = endX + stroke.getLineWidth() * 0.5f / arrowRatio;

        // vee
        Path2D.Float path = new Path2D.Float();

        float waisting = 0.5f;

        float waistX = endX + arrowLength * 0.5f;
        float waistY = arrowRatio * arrowLength * 0.5f * waisting;
        float arrowWidth = arrowRatio * arrowLength;

        path.moveTo(veeX + arrowLength, endY - arrowWidth);
        path.quadTo(waistX, endY - waistY, endX, endY);
        path.quadTo(waistX, endY + waistY, veeX + arrowLength, endY + arrowWidth);

        // end of arrow is pinched in
        path.lineTo(veeX + arrowLength * 0.75f, endY);
        path.lineTo(veeX + arrowLength, endY - arrowWidth);

        g.fill(path);

        // move stem back a bit
        g.draw(new Line2D.Float(startX, startY, veeX + arrowLength * 0.5f, endY));
    }

    public static void drawDX(Graphics2D g, int startX, int startY, int endX, int endY) {
        // as we're filling rather than stroking, control point is at the apex,

        float arrowRatio = 0.5f;
        float arrowLength = 9.0f;

        BasicStroke stroke = (BasicStroke) g.getStroke();

        //float endX = 350.0f;

        float veeX = endX - stroke.getLineWidth() * 0.5f / arrowRatio;

        // vee
        Path2D.Float path = new Path2D.Float();

        float waisting = 0.5f;

        float waistX = endX - arrowLength * 0.5f;
        float waistY = arrowRatio * arrowLength * 0.5f * waisting;
        float arrowWidth = arrowRatio * arrowLength;

        path.moveTo(veeX - arrowLength, endY - arrowWidth);
        path.quadTo(waistX, endY - waistY, endX, endY);
        path.quadTo(waistX, endY + waistY, veeX - arrowLength, endY + arrowWidth);

        // end of arrow is pinched in
        path.lineTo(veeX - arrowLength * 0.75f, endY);
        path.lineTo(veeX - arrowLength, endY - arrowWidth);

        g.fill(path);

        // move stem back a bit
        g.draw(new Line2D.Float(startX, startY, veeX - arrowLength * 0.5f, endY));
    }
}
